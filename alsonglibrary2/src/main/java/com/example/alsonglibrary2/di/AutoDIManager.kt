package com.example.alsonglibrary2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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
    val dependencies: MutableMap<KClass<*>, Any?> = mutableMapOf()

    var provider: LibraryDependencyProvider? = null

    inline fun <reified T : Any> registerDependency(dependency: Any) {
        dependencies[T::class] = dependency
    }

    inline fun <reified VM : ViewModel> createViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                createAutoDIInstance<VM>()
            }
        }
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
        val parametersWithAnnotation = constructor.parameters.filter { it.annotations.isNotEmpty() }
        for (parameter in parametersWithAnnotation) {
            val annotation =
                parameter.annotations.find { it.annotationClass.findAnnotation<AlsongQualifier>() != null }
                    ?: continue
            args[parameter] = findQualifierDependency(annotation) ?: continue
        }
        return constructor.callBy(args)
    }

    inline fun <reified T : Any> injectField(instance: T): T {
        val updatedDependencies = dependencies
        val properties = T::class.declaredMemberProperties
        val mutableProperties = properties.filterIsInstance<KMutableProperty<*>>()
        val fieldInjectProperties =
            mutableProperties.filter { it.findAnnotation<FieldInject>() != null }
        fieldInjectProperties.forEach { property ->
            changeQualifierDependency(property, updatedDependencies)
            property.isAccessible = true
            property.setter.call(
                instance,
                updatedDependencies[property.returnType.jvmErasure],
            )
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
        val dependencyProvider = provider ?: throw IllegalArgumentException()
        val targetFunction =
            dependencyProvider::class.memberFunctions
                .find { it.findAnnotation<Annotation>() == annotation } ?: return null
        return targetFunction.call(dependencyProvider)
    }
}
