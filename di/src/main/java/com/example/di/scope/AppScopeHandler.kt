package com.example.di.scope

import androidx.lifecycle.SavedStateHandle
import com.example.di.DependencyInjector
import com.example.di.DependencyKey
import kotlin.reflect.KClass

object AppScopeHandler : BaseScopeHandler() {
    override val scopeAnnotation: KClass<out Annotation> = AppScope::class
    private val instances = mutableMapOf<KClass<*>, Any>()

    init {
        ScopeContainer.setHandler(scopeAnnotation, this)
    }

    override fun <T : Any> getInstance(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle?,
        context: Any?,
    ): T =
        instances.getOrPut(kClass) {
            DependencyInjector.createInstance(kClass, savedStateHandle, DependencyKey(kClass, null))
        } as T
}
