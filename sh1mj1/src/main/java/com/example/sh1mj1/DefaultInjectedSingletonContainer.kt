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
        check(!cachedComponents.containsKey(componentKey)) { "There is already a component for $componentKey" }
        cachedComponents[componentKey] = component
    }

    override fun find(clazz: KClass<*>): Any? {
        val allComponentsQualifier = components.map { it.qualifier }
        // 중복된 value가 있는지 확인
        val duplicateValues =
            allComponentsQualifier
                .filterNotNull() // null 제거 (만약 qualifier가 null일 수 있으면)
                .groupBy { it.value } // value 기준으로 그룹핑
                .filter { it.value.size > 1 } // 그룹의 크기가 1보다 큰 경우만 필터링
                .keys // 중복된 value 값들
        check(duplicateValues.isEmpty()) { "There are duplicated Qualifier value: $duplicateValues" }

        return components.find {
            clazz.isSuperclassOf(it.injectedClass)
        }?.instance
    }

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier?,
    ): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass) && qualifier?.value == it.qualifier?.value
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
