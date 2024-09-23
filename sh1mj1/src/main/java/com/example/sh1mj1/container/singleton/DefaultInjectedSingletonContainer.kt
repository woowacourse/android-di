package com.example.sh1mj1.container.singleton

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.component.singleton.InjectedSingletonComponent
import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

class DefaultInjectedSingletonContainer private constructor() : InjectedSingletonContainer {
    private val components: MutableMap<ComponentKey, InjectedSingletonComponent<*>> =
        mutableMapOf()

    override fun <T : Any> add(component: InjectedSingletonComponent<T>) {
        val componentKey =
            ComponentKey.of(
                clazz = component.injectedClass,
                qualifier = component.qualifier,
            )
        if (components.containsKey(componentKey))
            {
                // TODO: 그냥 예외를 던지고, 테스트에서는 application 을 아예 다시 실행하는 옵션을 주는 건? application 이 가진 컨테이너가 초기화되지가 않네..
                return
            }
        components[componentKey] = component
    }

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier?,
    ): Any? =
        find(
            ComponentKey.of(
                clazz = clazz,
                qualifier = qualifier,
            ),
        )

    override fun find(componentKey: ComponentKey): Any? {
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

    override fun clear() {
        components.clear()
    }

    companion object {
        val instance = DefaultInjectedSingletonContainer()
    }
}
