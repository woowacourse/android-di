package com.kmlibs.supplin.base

import com.kmlibs.supplin.FunctionContainer
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
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

abstract class ComponentContainer(vararg modules: KClass<*>) {
    private val functionContainer = FunctionContainer(*modules)
    private val _instances = mutableMapOf<QualifiedType, Any>()
    val instances: Map<QualifiedType, Any>
        get() = _instances.toMap()

    init {
        modules.forEach(::saveModuleInstance)
    }

    abstract fun resolveInstance(
        returnType: KType,
        annotations: List<Annotation>,
    ): Any

    private fun saveModuleInstance(module: KClass<*>) {
        val moduleInstance = module.objectInstance ?: return
        val typeParameters = module.typeParameters
        val type =
            if (typeParameters.isNotEmpty()) {
                val arguments =
                    typeParameters.map { KTypeProjection.invariant(Any::class.createType()) }
                module.createType(arguments)
            } else {
                module.createType()
            }
        _instances[QualifiedType(type)] = moduleInstance
    }

    protected fun saveInstancesFromModuleFunctions() {
        functionContainer.qualifiedFunctions.forEach { (qualifiedType, function) ->
            if (function.hasAnnotation<Concrete>()) {
                resolveInstance(qualifiedType.returnType, function.annotations)
            }
        }
    }

    private fun <T : Any> qualifiedInstanceOf(qualifiedType: QualifiedType): T? {
        val instance = _instances[qualifiedType]
        return instance as T?
    }

    private fun qualifiedTypeOf(
        kType: KType,
        qualifierAnnotation: Annotation?,
    ): QualifiedType {
        val qualifier = qualifierAnnotation?.annotationClass?.simpleName
        return QualifiedType(kType, qualifier)
    }

    fun <T : Any> instanceOf(kClass: KClass<T>): T {
        val targetConstructor =
            kClass.constructors.firstOrNull { constructor ->
                constructor.hasAnnotation<Supply>()
            } ?: error(EXCEPTION_NO_MATCHING_FUNCTION.format(kClass.simpleName))

        return qualifiedInstanceOf(QualifiedType(targetConstructor.returnType, null))
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

    fun <T : Any> instanceOf(kParameter: KParameter): T {
        val annotation = findAnnotationOf<Qualifier>(kParameter.annotations)
        return qualifiedInstanceOf(
            QualifiedType(
                returnType = kParameter.type,
                qualifier = annotation?.annotationClass?.simpleName,
            ),
        ) ?: createInstance(kParameter.type.jvmErasure, annotation) as T
    }

    private fun <T : Any> instanceOf(kType: KType): T {
        val qualifierAnnotation = findAnnotationOf<Qualifier>(kType.annotations)
        val qualifiedType = qualifiedTypeOf(kType, qualifierAnnotation)

        return qualifiedInstanceOf(qualifiedType) ?: createInstance(
            kType.jvmErasure,
            findAnnotationOf<Qualifier>(kType.jvmErasure.annotations),
        ) as T
    }

    private fun <T : Any> createInstance(
        kClass: KClass<T>,
        qualifierAnnotation: Annotation?,
    ): T {
        val targetConstructor =
            kClass.constructors.firstOrNull { constructor ->
                constructor.hasAnnotation<Supply>()
            }

        return if (targetConstructor != null) {
            createInstanceOfImpl(targetConstructor, kClass)
        } else {
            createInstanceOfAbstraction(kClass, qualifierAnnotation)
        }
    }

    private fun <T : Any> createInstanceOfAbstraction(
        kClass: KClass<T>,
        qualifierAnnotation: Annotation?,
    ): T {
        val function =
            functionContainer.qualifiedFunctions[
                qualifiedTypeOf(
                    kClass.createType(),
                    qualifierAnnotation,
                ),
            ]
        requireNotNull(function) {
            EXCEPTION_NO_MATCHING_FUNCTION.format(
                kClass.createType().jvmErasure.simpleName,
                qualifierAnnotation
            )
        }
        val parameterValues = resolveParameterValues(function)
        val instance = function.callBy(parameterValues)
        checkNotNull(instance) {
            EXCEPTION_NULL_INSTANCE.format(
                kClass.createType().jvmErasure.simpleName,
                qualifierAnnotation
            )
        }
        return instance as T
    }

    private fun <T : Any> createInstanceOfImpl(
        targetConstructor: KFunction<T>,
        kClass: KClass<T>,
    ): T {
        val parameters =
            targetConstructor.parameters.associateWith { param ->
                instanceOf(param.type.classifier as KClass<*>)
            }

        val instance = targetConstructor.callBy(parameters)
        injectFields(kClass, instance)
        _instances[QualifiedType(targetConstructor.returnType, null)] = instance

        return instance
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

    fun injectSingleField(
        targetField: KProperty1<*, *>,
        instance: Any,
    ) {
        targetField.isAccessible = true
        try {
            (targetField as KMutableProperty<*>).setter.call(instance, instanceOf(targetField))
        } catch (e: IllegalStateException) {
            val property = instanceOf(targetField.returnType.jvmErasure)
            (targetField as KMutableProperty<*>).setter.call(instance, property)
        }
    }

    private fun resolveParameterValues(function: KFunction<*>): Map<KParameter, Any> {
        return function.parameters.associateWith { parameter ->
            resolveInstance(parameter.type, parameter.annotations)
        }
    }

    protected fun buildQualifiedType(
        returnType: KType,
        annotations: List<Annotation>,
    ): QualifiedType {
        val qualifierAnnotation =
            annotations.firstOrNull { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
        return QualifiedType(returnType, qualifierAnnotation?.annotationClass?.simpleName)
    }

    protected fun buildInstanceOf(qualifiedType: QualifiedType): Any? {
        val function = functionContainer.qualifiedFunctions[qualifiedType]
        requireNotNull(function) { EXCEPTION_NO_MATCHING_FUNCTION.format(qualifiedType.returnType) }

        val parameterValues = resolveParameterValues(function)
        val instance = function.callBy(parameterValues)

        if (instance != null) {
            _instances[qualifiedType] = instance
        }
        return instance
    }

    private inline fun <reified T : Annotation> findAnnotationOf(annotations: List<Annotation>): Annotation? {
        return annotations.firstOrNull { annotation ->
            annotation.annotationClass.hasAnnotation<T>()
        }
    }

    companion object {
        private const val EXCEPTION_NO_MATCHING_FUNCTION =
            "No function found for return type: %s - %s"
        private const val EXCEPTION_NULL_INSTANCE =
            "Failed to create instance for the function: %s - %s"
    }
}
