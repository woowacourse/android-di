package com.woosuk.scott_di_android

import androidx.lifecycle.ViewModel
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

open class DiViewModel : ViewModel() {
    private val module = DiApplication.module

    private val injectableTypes =
        this::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
            .map { it.returnType }

    private val dependencies =
        module::class.declaredMemberFunctions
            .filter { it.hasAnnotation<ViewModelScope>() }
            .filter { injectableTypes.contains(it.returnType) }
            .map { Dependency(module, it, null) }

    private fun destroyFields() {
        val properties = this::class
            .memberProperties.filter { it.hasAnnotation<Inject>() }
        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.set(this, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        destroyFields()
        dependencies.forEach { DiContainer.destroyDependency(it) }
    }
}
