package com.kmlibs.supplin

import android.content.Context
import com.kmlibs.supplin.annotations.Abstract
import com.kmlibs.supplin.annotations.ApplicationContext
import com.kmlibs.supplin.annotations.Concrete
import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.model.QualifiedType
import javax.inject.Qualifier
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class InstanceContainer(
    private val applicationContext: Context,
    modules: List<KClass<*>>,
) {
    private val functionContainer = FunctionContainer(modules)
    private val instances: MutableMap<QualifiedType, Any> = mutableMapOf()

    init {
        saveModuleInstances(modules.mapNotNull { it.objectInstance })
        saveInstancesFromModuleFunctions()
    }

    fun <T : Any> instanceOf(kParameter: KParameter): T {
        val annotation = findAnnotationOf<Qualifier>(kParameter.annotations)
        return qualifiedInstanceOf(
            QualifiedType(
                returnType = kParameter.type,
                qualifier = annotation?.annotationClass?.simpleName,
            ),
        ) ?: createInstance(kParameter.type.jvmErasure, annotation) as T
    }

    fun <T : Any> instanceOf(kClassifier: KClassifier): T {
        return instanceOf(kClassifier.createType())
    }

    fun <T : Any> instanceOf(kClass: KClass<T>): T {
        val targetConstructor =
            kClass.constructors.firstOrNull { constructor ->
                constructor.hasAnnotation<Supply>()
            } ?: error(EXCEPTION_NO_MATCHING_FUNCTION.format(kClass.simpleName))

        return instances[QualifiedType(targetConstructor.returnType, null)] as? T
            ?: createInstance(kClass, findAnnotationOf<Qualifier>(kClass.annotations))
    }

    private fun <T : Any> instanceOf(kCallable: KCallable<*>): T {
        val annotation = findAnnotationOf<Qualifier>(kCallable.annotations)
        return qualifiedInstanceOf(
            QualifiedType(
                returnType = kCallable.returnType,
                qualifier = annotation?.annotationClass?.simpleName,
            ),
        ) ?: createInstance(kCallable.returnType.jvmErasure, annotation) as T
    }

    private fun <T : Any> instanceOf(kType: KType): T {
        val qualifierAnnotation = findAnnotationOf<Qualifier>(kType.annotations)
        val qualifiedType = qualifiedTypeOf(kType, qualifierAnnotation)

        return qualifiedInstanceOf(qualifiedType) ?: createInstance(kType.jvmErasure, findAnnotationOf<Qualifier>(kType.jvmErasure.annotations)) as T
    }

    private fun <T : Any> createInstance(kClass: KClass<T>, qualifierAnnotation: Annotation?): T {
        val targetConstructor =
            kClass.constructors.firstOrNull { constructor ->
                constructor.hasAnnotation<Supply>()
            }

        if (targetConstructor != null) {
            val parameters =
                targetConstructor.parameters.associateWith { param ->
                    instanceOf(param.type.classifier as KClass<*>)
                }

            val instance = targetConstructor.callBy(parameters)
            injectFields(kClass, instance)
            instances[QualifiedType(targetConstructor.returnType, null)] = instance

            return instance
        } else {
            val function = functionContainer.qualifiedFunctions[qualifiedTypeOf(kClass.createType(), qualifierAnnotation)]
            requireNotNull(function) { EXCEPTION_NO_MATCHING_FUNCTION.format(kClass.simpleName) }
            val parameterValues = resolveParameterValues(function)
            val instance = function.callBy(parameterValues)
            checkNotNull(instance) { EXCEPTION_NULL_INSTANCE.format(function.name) }
            return instance as T
        }
    }

    fun <T : Any> injectFields(
        kClass: KClass<T>,
        instance: T,
    ) {
        kClass.memberProperties.filter { field ->
            field.hasAnnotation<Supply>()
        }.forEach { targetField ->
            injectSingleField(targetField, instance)
        }
    }

    private fun <T : Any> injectSingleField(
        targetField: KProperty1<T, *>,
        instance: T,
    ) {
        targetField.isAccessible = true
        try {
            (targetField as KMutableProperty<*>).setter.call(instance, instanceOf(targetField))
        } catch (e: IllegalStateException) {
            val property = instanceOf(targetField.returnType.jvmErasure)
            (targetField as KMutableProperty<*>).setter.call(instance, property)
        }
    }

    private fun saveModuleInstances(modules: List<Any>) {
        modules.forEach { module ->
            instances[QualifiedType(module::class.createType())] = module
        }
    }

    private fun saveInstancesFromModuleFunctions() {
        functionContainer.qualifiedFunctions.forEach { (qualifiedType, function) ->
            if (function.hasAnnotation<Concrete>()) {
                resolveInstance(qualifiedType.returnType, function.annotations)
            }
        }
    }

    private fun qualifiedTypeOf(
        kType: KType,
        qualifierAnnotation: Annotation?,
    ): QualifiedType {
        val qualifier = qualifierAnnotation?.annotationClass?.simpleName
        return QualifiedType(kType, qualifier)
    }

    private fun <T : Any> qualifiedInstanceOf(qualifiedType: QualifiedType): T? {
        val instance = instances[qualifiedType]
        return instance as T?
    }

    private fun resolveInstance(
        returnType: KType,
        annotations: List<Annotation>,
    ): Any {
        if (shouldResolveContext(returnType, annotations)) return applicationContext
        val qualifiedType = buildQualifiedType(returnType, annotations)
        return instances[qualifiedType] ?: buildInstanceOf(qualifiedType)
    }

    private fun shouldResolveContext(
        returnType: KType,
        annotations: List<Annotation>,
    ): Boolean {
        val contextAnnotation = annotations.filterIsInstance<ApplicationContext>().firstOrNull()
        return contextAnnotation != null && returnType.classifier == Context::class
    }

    private fun buildQualifiedType(
        returnType: KType,
        annotations: List<Annotation>,
    ): QualifiedType {
        val qualifierAnnotation =
            annotations.firstOrNull { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
        return QualifiedType(returnType, qualifierAnnotation?.annotationClass?.simpleName)
    }

    private fun buildInstanceOf(qualifiedType: QualifiedType): Any {
        val function = functionContainer.qualifiedFunctions[qualifiedType]
        requireNotNull(function) { EXCEPTION_NO_MATCHING_FUNCTION.format(qualifiedType.returnType) }

        val parameterValues = resolveParameterValues(function)
        val instance = function.callBy(parameterValues)
        checkNotNull(instance) { EXCEPTION_NULL_INSTANCE.format(function.name) }

        instances[qualifiedType] = instance
        return instance
    }

    private fun resolveParameterValues(function: KFunction<*>): Map<KParameter, Any> {
        return function.parameters.associateWith { parameter ->
            resolveInstance(parameter.type, parameter.annotations)
        }
    }

    private inline fun <reified T : Annotation> findAnnotationOf(annotations: List<Annotation>): Annotation? {
        return annotations.firstOrNull { annotation ->
            annotation.annotationClass.hasAnnotation<T>()
        }
    }

    companion object {
        private const val EXCEPTION_NO_MATCHING_PROPERTY =
            "No matching property found for parameter %s"
        private const val EXCEPTION_NO_MATCHING_FUNCTION =
            "No function found for return type: %s"
        private const val EXCEPTION_NULL_INSTANCE =
            "Failed to create instance for the function: %s"
    }
}
