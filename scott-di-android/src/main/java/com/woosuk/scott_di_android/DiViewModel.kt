package com.woosuk.scott_di_android

import androidx.lifecycle.ViewModel
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

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

    override fun onCleared() {
        super.onCleared()
        dependencies.forEach { DiContainer.destroyDependency(it) }
    }
}
