package com.example.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.di.annotations.Inject
import com.example.di.annotations.Qualifier
import com.example.di.annotations.ViewModelScope
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

inline fun <reified VM : ViewModel> injectViewModels(componentActivity: ComponentActivity): Lazy<VM> =
    componentActivity.viewModels {
        viewModelFactory<VM>()
    }

inline fun <reified VM : ViewModel> viewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            injectViewModel<VM>()
        }
    }

inline fun <reified T : ViewModel> injectViewModel(): T {
    val injectableTypes =
        T::class
            .declaredMemberProperties
            .filter { it.hasAnnotation<Inject>() }
            .map { it.returnType }

    val dependencies =
        DIApplication.module::class
            .declaredMemberFunctions
            .filter { it.hasAnnotation<ViewModelScope>() }
            .filter { injectableTypes.contains(it.returnType) }
            .map { Dependency(DIApplication.module, it) }

    dependencies.forEach { DIContainer.addDependency(it) }
    return injectConstructor(T::class)
}

fun <T : Any> injectConstructor(kClazz: KClass<T>): T {
    val primaryConstructor =
        kClazz.primaryConstructor
            ?: throw IllegalStateException("${kClazz.simpleName} No primary constructor found")

    val constructorParameters = primaryConstructor.parameters

    val instances =
        constructorParameters
            .map { parameter ->
                DIContainer.getDependencyInstance(
                    parameter.type.jvmErasure,
                    parameter.getQualifierAnnotation(),
                )
            }.toTypedArray()
    return primaryConstructor.call(*instances)
}

fun KParameter.getQualifierAnnotation() = annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

fun KProperty<*>.getQualifierAnnotation() = annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

fun KFunction<*>.getQualifierAnnotation() = annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
