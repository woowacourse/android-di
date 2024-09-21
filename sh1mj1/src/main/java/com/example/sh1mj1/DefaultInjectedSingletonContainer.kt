package com.example.sh1mj1

import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.isSuperclassOf

object DefaultInjectedSingletonContainer : InjectedSingletonContainer {
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
        cachedComponents[componentKey] = component
    }

    override fun find(clazz: KClass<*>): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass)
        }?.instance

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier,
    ): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass) && qualifier.value == it.qualifier?.value
        }?.instance

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
}

private const val TAG = "DefaultInjectedSingleto"
