package com.example.alsonglibrary2.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object AutoDIManager {
    private val _dependencies: MutableMap<KClass<*>, Any?> = mutableMapOf()
    val dependencies: Map<KClass<*>, Any?> get() = _dependencies

    var qualifierDependencyProvider: QualifierDependencyProvider? = null

    inline fun <reified T : Any> registerDependency(dependency: Any) {
        setDependency(T::class, dependency)
    }

    fun setDependency(
        kClass: KClass<*>,
        dependency: Any,
    ) {
        _dependencies[kClass] = dependency
    }

    fun clearDependencies() {
        _dependencies.clear()
    }

    inline fun <reified T : Any> removeDependency() {
        (dependencies as MutableMap<KClass<*>, Any?>).remove(T::class)
    }

    inline fun <reified T : Any> createAutoDIInstance(): T {
        val constructorInjectedInstance = injectConstructor<T>()
        val fieldInjectedInstance = injectField<T>(constructorInjectedInstance)

        return fieldInjectedInstance
    }

    inline fun <reified T : Any> injectConstructor(): T {
        val clazz = T::class
        val constructor = clazz.primaryConstructor ?: return clazz.createInstance()
        val args =
            constructor.parameters.associateWith { dependencies[it.type.jvmErasure] }.toMutableMap()
        for (parameter in constructor.parameters) {
            val annotation =
                parameter.annotations.find { it.annotationClass.findAnnotation<AlsongQualifier>() != null }
                    ?: continue
            args[parameter] = findQualifierDependency(annotation) ?: continue
        }
        return constructor.callBy(args)
    }

    inline fun <reified T : Any> injectField(instance: T): T {
        val qualifiedDependencies = dependencies.toMutableMap()
        val properties = T::class.declaredMemberProperties
        val mutableProperties = properties.filterIsInstance<KMutableProperty<*>>()
        val fieldInjectProperties =
            mutableProperties.filter { it.findAnnotation<FieldInject>() != null }
        fieldInjectProperties.forEach { property ->
            changeQualifierDependency(property, qualifiedDependencies)
            property.isAccessible = true
            property.setter.call(instance, qualifiedDependencies[property.returnType.jvmErasure])
        }
        return instance
    }

    fun changeQualifierDependency(
        property: KMutableProperty<*>,
        updatedDependencies: MutableMap<KClass<*>, Any?>,
    ) {
        property.annotations.find { it.annotationClass.findAnnotation<AlsongQualifier>() != null }
            ?.let { qualifierAnnotation ->
                updatedDependencies[property.returnType.jvmErasure] =
                    findQualifierDependency(qualifierAnnotation)
            }
    }

    fun findQualifierDependency(annotation: Annotation): Any? {
        val dependencyProvider = qualifierDependencyProvider ?: throw IllegalArgumentException()
        val targetFunction =
            dependencyProvider::class.memberFunctions
                .find { it.findAnnotation<Annotation>() == annotation } ?: return null
        return targetFunction.call(dependencyProvider)
    }
}
