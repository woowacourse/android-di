package com.example.sh1mj1.container.viewmodelscope

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.viewmodelscope.ViewModelScopeComponent
import kotlin.reflect.KClass

class ViewModelComponentContainer private constructor() {
    private val components = mutableListOf<ViewModelScopeComponent<*>>()

    fun find(clazz: KClass<*>, qualifier: Qualifier?): Any {
        return components.find {
            it.injectedClass == clazz && it.qualifier == qualifier
        } ?: throw IllegalStateException("${clazz.simpleName} not found in ViewModelContainer")
    }

    companion object {
        private var instance: ViewModelComponentContainer? = null

        fun instance(): ViewModelComponentContainer {
            if (instance == null) {
                instance = ViewModelComponentContainer()
            }
            return instance!!
        }
    }
}
