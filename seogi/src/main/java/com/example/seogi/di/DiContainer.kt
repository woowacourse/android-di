package com.example.seogi.di

import com.example.seogi.di.util.getAnnotationIncludeQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
class DiContainer(
    private val diModule: Module,
) {
    private val dependencies: MutableMap<DependencyKey, Any> = mutableMapOf()
    private val functions: Collection<KFunction<*>> by lazy {
        diModule::class.declaredFunctions.filter { it.visibility == KVisibility.PUBLIC }
    }

    fun <T : Any> getInstance(
        clazz: KClass<T>,
        annotation: Annotation?,
    ): T {
        return (
            dependencies[DependencyKey(clazz, annotation)] ?: addDependency(
                functions,
                clazz,
                annotation,
            )
        ) as T
    }

    private fun addDependency(
        functions: Collection<KFunction<*>>,
        clazz: KClass<*>,
        annotation: Annotation?,
    ): Any {
        val sameTypeFunctions =
            functions.filter {
                (it.returnType.jvmErasure == clazz)
            }
        val func = findFunction(annotation, sameTypeFunctions)

        if (func.valueParameters.isEmpty()) {
            addInstance(clazz, annotation, func.call(diModule))
            return getInstance(clazz, annotation)
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
        return getInstance(clazz, annotation)
    }

    private fun findFunction(
        annotation: Annotation?,
        newFunc: List<KFunction<*>>,
    ) = if (annotation != null) {
        newFunc.first { it.annotations.contains(annotation) }
    } else {
        newFunc.first()
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
