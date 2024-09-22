package com.example.sh1mj1.container

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.ComponentKey
import com.example.sh1mj1.component.InjectedSingletonComponent
import com.example.sh1mj1.component.InjectedSingletonContainer
import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

class DefaultInjectedSingletonContainer private constructor() : InjectedSingletonContainer {
    private val components: MutableMap<ComponentKey, InjectedSingletonComponent> =
        mutableMapOf()

    override fun add(component: InjectedSingletonComponent) {
        val componentKey =
            ComponentKey.of(
                clazz = component.injectedClass,
                qualifier = component.qualifier,
            )
        check(!components.containsKey(componentKey)) { "There is already a component for $componentKey" }
        components[componentKey] = component
    }

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier?,
    ): Any =
        find(
            ComponentKey.of(
                clazz = clazz,
                qualifier = qualifier,
            ),
        )

    override fun find(componentKey: ComponentKey): Any {
        val foundComponent = components[componentKey]
        val foundInstance =
            foundComponent?.instance ?: throw IllegalStateException("There is no component for $componentKey")

        if (foundComponent.qualifier?.generate == true) {
            return foundInstance
        }

        foundComponent.injectableProperties().forEach { kProperty ->
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

    override fun clear() {
        components.clear()
    }

    companion object {
        val instance = DefaultInjectedSingletonContainer()
    }
}
