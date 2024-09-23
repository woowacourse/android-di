package com.example.sh1mj1.container.viewmodelscope

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.component.viewmodelscope.ViewModelScopeComponent
import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

class ViewModelComponentContainer private constructor() {
    private val components = mutableListOf<ViewModelScopeComponent<*>>()

    /*
    private val components: MutableMap<ComponentKey, InjectedSingletonComponent<*>> =
        mutableMapOf()
     */

    private val cachedComponents = mutableMapOf<ComponentKey, ViewModelScopeComponent<*>>()

    fun add(component: ViewModelScopeComponent<*>) {
        if (components.contains(component)) {
            throw IllegalStateException("ViewModelComponentContainer already contains ${component.injectedClass.simpleName}")
        }
        components.add(component)

        // cache
        val componentKey = ComponentKey.of(component.injectedClass, component.qualifier)
        cachedComponents[componentKey] = component
    }

    fun find(clazz: KClass<*>, qualifier: Qualifier?): Any? = find(ComponentKey.of(clazz, qualifier))

    fun remove(clazz: KClass<*>, qualifier: Qualifier?) {
        ComponentKey.of(clazz, qualifier).let {
            components.remove(cachedComponents[it])
            cachedComponents.remove(it)
        }
    }

    fun remove(component: ViewModelScopeComponent<*>) {
        components.remove(component)
    }

    fun all(): List<ViewModelScopeComponent<*>> {
        return components
    }

    fun find(componentKey: ComponentKey): Any? {
        val foundComponent = cachedComponents[componentKey]
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

    fun remove(clazz: KClass<*>) {
        components.removeIf { it.injectedClass == clazz }
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

private const val TAG = "ViewModelComponentConta"