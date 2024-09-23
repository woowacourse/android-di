package com.example.sh1mj1.container.viewmodelscope

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.component.viewmodelscope.ViewModelScopeComponent
import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

class ViewModelComponentContainer private constructor() {
    private val components = mutableMapOf<ComponentKey, ViewModelScopeComponent<*>>()

    fun add(component: ViewModelScopeComponent<*>) {
        val componentKey = ComponentKey.of(component.injectedClass, component.qualifier)
        components[componentKey] = component
    }

    fun find(clazz: KClass<*>, qualifier: Qualifier?): Any? = find(ComponentKey.of(clazz, qualifier))

    fun remove(clazz: KClass<*>, qualifier: Qualifier?) {
        remove(ComponentKey.of(clazz, qualifier))
    }

    fun remove(componentKey: ComponentKey) {
        components.remove(componentKey)
    }

    fun find(componentKey: ComponentKey): Any? {
        val foundComponent = components[componentKey]
        val foundInstance =
            foundComponent?.instance

        if (foundComponent?.qualifier?.generate == true) {
            return foundInstance
        }

        foundComponent?.injectableProperties()?.forEach { kProperty ->
            val dependency =
                find(
                    ComponentKey.of(
                        clazz = kProperty.returnType.classifier as KClass<*>,
                        qualifier = kProperty.withQualifier(),
                    ),
                )

            (kProperty as KMutableProperty<*>).setter.call(foundInstance, dependency)
        }

        return foundInstance
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
