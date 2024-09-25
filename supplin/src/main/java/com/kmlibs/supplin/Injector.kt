package com.kmlibs.supplin

import com.kmlibs.supplin.base.ComponentContainer
import kotlin.reflect.KClass

object Injector {
    lateinit var componentContainers: Map<KClass<*>, ComponentContainer>
        private set
    private lateinit var injectionBuilder: InjectionBuilder

    fun setModules(block: InjectionBuilder.() -> Unit) {
        if (!::injectionBuilder.isInitialized) {
            injectionBuilder = InjectionBuilder()
        }
        componentContainers = injectionBuilder.apply(block).build()
    }
}
