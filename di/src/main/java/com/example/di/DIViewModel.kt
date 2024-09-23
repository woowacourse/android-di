package com.example.di

import androidx.lifecycle.ViewModel
import com.example.di.annotations.Inject
import com.example.di.annotations.ViewModelScope
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

open class DIViewModel : ViewModel() {
    private val module = DIApplication.module

    private val injectableTypes =
        this::class
            .declaredMemberProperties
            .filter { it.hasAnnotation<Inject>() }
            .map { it.returnType }

    private val dependencies =
        module::class
            .declaredMemberFunctions
            .filter { it.hasAnnotation<ViewModelScope>() }
            .filter { injectableTypes.contains(it.returnType) }
            .map { Dependency(module, it, null) }

    override fun onCleared() {
        super.onCleared()
        destroyFields()
        dependencies.forEach { DIContainer.destroyDependency(it) }
    }

    private fun destroyFields() {
        val properties =
            this::class
                .memberProperties
                .filter { it.hasAnnotation<Inject>() }
        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.set(this, null)
        }
    }
}
