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
            val annotation = parameter.annotations.find { it.annotationClass.findAnnotation<AlsongQualifier>() != null } ?: continue
            args[parameter] = fetchQualifierDependency(annotation) ?: continue
        }
        return constructor.callBy(args)
    }

    inline fun <reified T : Any> injectField(instance: T): T {
        val updatedDependencies = dependencies
        val properties = T::class.declaredMemberProperties
        val mutableProperties = properties.filterIsInstance<KMutableProperty<*>>()
        val fieldInjectProperties =
            mutableProperties.filter { it.findAnnotation<FieldInject>() != null }
        fieldInjectProperties.forEach { fieldInjectProperty ->
            changeQualifierDependency(fieldInjectProperty, updatedDependencies)
            fieldInjectProperty.isAccessible = true
            fieldInjectProperty.setter.call(
                instance,
                updatedDependencies[fieldInjectProperty.returnType.jvmErasure],
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
                    fetchQualifierDependency(qualifierAnnotation)
            }
    }

    /**
     * Qualifier 어노테이션이 붙은 함수를 DependencyProvider에서 찾아서 호출합니다.
     **/
    inline fun <reified A : Annotation> fetchQualifierDependency(annotation: A): Any? {
        val dependencyProvider = provider ?: return null
        val targetFunction =
            dependencyProvider::class.memberFunctions
                .find { it.findAnnotation<A>() == annotation } ?: return null
        return targetFunction.call(dependencyProvider)
    }
}
