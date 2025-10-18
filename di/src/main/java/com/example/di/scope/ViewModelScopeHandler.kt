package com.example.di.scope

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.di.DependencyInjector
import com.example.di.DependencyKey
import kotlin.reflect.KClass

object ViewModelScopeHandler : BaseScopeHandler() {
    override val scopeAnnotation = ViewModelScope::class
    private val instances = mutableMapOf<ViewModel, MutableMap<KClass<*>, Any>>()

    init {
        ScopeContainer.setHandler(scopeAnnotation, this)
    }

    override fun <T : Any> getInstance(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle?,
        context: Any?,
    ): T {
        val viewModel = context as ViewModel
        val container = instances.getOrPut(viewModel) { mutableMapOf() }

        return container.getOrPut(kClass) {
            DependencyInjector.createInstance(
                kClass,
                savedStateHandle,
                DependencyKey(kClass, null),
            )
        } as T
    }

    fun removeInstance(viewModel: ViewModel) {
        instances.remove(viewModel)
    }
}
