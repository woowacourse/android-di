package com.example.seogi.di

import android.content.Context
import com.example.seogi.di.annotation.FieldInject
import com.example.seogi.di.util.getAnnotationIncludeQualifier
import com.example.seogi.di.util.hasSingleToneAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
class DiContainer(
    private val diModule: Module,
    context: Context,
) {
    private val dependencies: MutableMap<DependencyKey, Any> = mutableMapOf()
    private val functions: Collection<KFunction<*>> by lazy {
        diModule::class.declaredFunctions.filter { it.visibility == KVisibility.PUBLIC }
    }

    init {
        dependencies[DependencyKey(Context::class, null)] = context

        functions.forEach {
            addDependency(it.returnType.jvmErasure, it.getAnnotationIncludeQualifier())
        }
    }

    fun <T : Any> instance(clazz: KClass<T>): T {
        val constructor = clazz.primaryConstructor ?: throw IllegalArgumentException()

        val params =
            constructor.parameters.map { param ->
                val annotation = param.getAnnotationIncludeQualifier()
                getInstance(param.type.jvmErasure, annotation)
            }
        val instance = constructor.call(*params.toTypedArray())
        inject(instance)
        return instance
    }

    fun removeDependency(func: KFunction<*>) {
        val returnTypeClass = func.returnType.jvmErasure
        val annotation = func.getAnnotationIncludeQualifier()
        val key = DependencyKey(returnTypeClass, annotation)

        if (dependencies.containsKey(key)) {
            dependencies.remove(key)
        }
    }

    private fun <T : Any> getInstance(
        clazz: KClass<T>,
        annotation: Annotation?,
    ): T {
        return (
                dependencies[DependencyKey(clazz, annotation)] ?: instance(clazz)
                ) as T
    }

    private fun addDependency(
        clazz: KClass<*>,
        annotation: Annotation?,
    ) {
        val sameTypeFunctions =
            functions.filter {
                (it.returnType.jvmErasure == clazz)
            }
        val func = findFunction(annotation, sameTypeFunctions)

        if (func.valueParameters.isEmpty()) {
            val instance = func.call(diModule) ?: throw IllegalArgumentException()
            func.injectFields(instance)
            addInstance(clazz, annotation, instance)
            return
        }
        val params =
            func.valueParameters.map { param ->
                val paramInstanceType = param.type.jvmErasure
                getInstance(
                    paramInstanceType,
                    param.getAnnotationIncludeQualifier(),
                )
            }
        addInstance(clazz, annotation, func.call(diModule, *params.toTypedArray()))
    }

    private fun KFunction<*>.injectFields(instance: Any) {
        if (!hasSingleToneAnnotation()) return
        inject(instance)
    }

    private fun inject(instance: Any) {
        val fields =
            instance::class.declaredMemberProperties.filter {
                it.annotations.contains(FieldInject())
            }.map { it as KMutableProperty1 }
        fields.forEach { field ->
            val qualifierAnnotation = field.getAnnotationIncludeQualifier()
            val value = getInstance(field.returnType.jvmErasure, qualifierAnnotation)
            field.isAccessible = true
            field.setter.call(instance, value)
        }
    }

    private fun findFunction(
        annotation: Annotation?,
        newFunc: List<KFunction<*>>,
    ) = if (annotation != null) {
        newFunc.firstOrNull { it.annotations.contains(annotation) }
            ?: throw IllegalArgumentException()
    } else {
        newFunc.firstOrNull() ?: throw IllegalArgumentException()
    }

    private fun addInstance(
        classType: KClass<*>,
        annotation: Annotation?,
        instance: Any?,
    ) {
        instance?.let {
            dependencies[DependencyKey(classType, annotation)] = it
        }
    }
}
