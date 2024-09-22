package com.example.sh1mj1.container

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.component.singleton.InjectedSingletonComponent
import com.example.sh1mj1.container.singleton.DefaultInjectedSingletonContainer
import com.example.sh1mj1.container.singleton.InjectedSingletonContainer
import kotlin.reflect.KClass

class DefaultAppContainer(
    private val singletonComponentContainer: InjectedSingletonContainer = DefaultInjectedSingletonContainer.instance,
) : AppContainer {
    override fun<T : Any>  add(component: InjectedSingletonComponent<T>) {
        singletonComponentContainer.add(component)
    }

    override fun add(vararg component: InjectedSingletonComponent<*>) {
        component.forEach {
            add(it)
        }
    }

    override fun <T : Any> find(
        clazz: KClass<T>,
        qualifier: Qualifier?,
    ): Any = find(ComponentKey.of(clazz, qualifier))

    override fun find(componentKey: ComponentKey): Any = singletonComponentContainer.find(componentKey)

    override fun clearActivityScopedObjects() {
    }
}
