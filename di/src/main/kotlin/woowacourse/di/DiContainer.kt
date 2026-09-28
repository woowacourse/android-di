package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.cast
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DiContainer {
    private val store = mutableMapOf<KClass<*>, Any>()
    private val interfaceRules = mutableMapOf<KClass<*>, KClass<*>>()

    fun <T : Any> instantiate(type: KClass<T>): T {
        val implementationType = interfaceRules[type] ?: type
        val constructor =
            requireNotNull(implementationType.primaryConstructor) {
                "생성자가 없습니다."
            }

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType = parameter.type.classifier as KClass<*>

                store.getOrPut(dependencyType) {
                    instantiate(dependencyType)
                }
            }

        return type.cast(constructor.call(*dependencies.toTypedArray()))
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
    ) {
        store[type] = instance
    }

    fun registerInterfaceRule(
        type: KClass<*>,
        implementationType: KClass<*>,
    ) {
        interfaceRules[type] = implementationType
    }

    fun inject(target: Any) {
        val annotatedProperties =
            target::class
                .declaredMemberProperties
                .filter { property -> property.annotations.any { it is FieldInject } }

        annotatedProperties.forEach { property ->
            val mutableProperty =
                property as? KMutableProperty1<*, *>
                    ?: error("주입 대상은 var여야 합니다")
            val dependencyType =
                mutableProperty.returnType.classifier as? KClass<*>
                    ?: error("주입 대상의 타입을 확인할 수 없습니다: ${property.name}")
            val dependency =
                store.getOrPut(dependencyType) {
                    instantiate(dependencyType)
                }

            mutableProperty.isAccessible = true
            mutableProperty.setter.call(target, dependency)
        }
    }
}
