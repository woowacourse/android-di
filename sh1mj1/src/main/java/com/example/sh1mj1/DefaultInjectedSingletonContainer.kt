package com.example.sh1mj1

import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

class DefaultInjectedSingletonContainer private constructor() : InjectedSingletonContainer {
    private val components: MutableList<InjectedComponent.InjectedSingletonComponent> = mutableListOf()

    private val cachedComponents: MutableMap<ComponentKey, InjectedComponent.InjectedSingletonComponent> =
        mutableMapOf()

    override fun add(component: InjectedComponent.InjectedSingletonComponent) {
        components.add(component)

        // cache component
        val componentKey =
            ComponentKey(
                clazz = component.injectedClass,
                qualifier = component.qualifier,
            )
        check(!cachedComponents.containsKey(componentKey)) { "There is already a component for $componentKey" }
        cachedComponents[componentKey] = component
    }

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier?,
    ): Any = findWithKey(
        ComponentKey(
            clazz = clazz,
            qualifier = qualifier,
        ),
    )

    override fun findWithKey(componentKey: ComponentKey): Any {
        val foundComponent = cachedComponents[componentKey]
        val foundInstance =
            foundComponent?.instance ?: throw IllegalStateException("There is no component for $componentKey")

        if (foundComponent.qualifier?.generate == true) {
            return foundInstance
        }

        foundComponent.injectableProperties().forEach { kProperty ->
            val dependency =
                findWithKey(
                    ComponentKey(
                        clazz = kProperty.returnType.classifier as KClass<*>,
                        qualifier = kProperty.withQualifier(),
                    ),
                )

            (kProperty as KMutableProperty<*>).setter.call(foundInstance, dependency)
        }

        return foundInstance
    }

    override fun clear() {
        components.clear()
        cachedComponents.clear()
    }

    companion object {
        val instance = DefaultInjectedSingletonContainer()
    }
}
