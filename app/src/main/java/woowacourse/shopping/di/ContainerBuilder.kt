package woowacourse.shopping.di

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

class ContainerBuilder {
    private val providers = mutableMapOf<KClass<*>, () -> Any>()

    fun <A : Any> register(
        abstractType: KClass<A>,
        provider: () -> A,
    ): ContainerBuilder =
        apply {
            providers[abstractType] = provider as () -> Any
        }

    fun build(): AppContainer {
        val providersSnapShot: Map<KClass<*>, () -> Any> = providers.toMap()

        return object : AppContainer {
            private val concreteCache = ConcurrentHashMap<KType, Any>()
            private val abstractCache = ConcurrentHashMap<KClass<*>, Any>()

            override fun resolve(type: KType): Any {
                val kClass =
                    type.classifier as? KClass<*>
                        ?: error("지원하지 않는 타입: $type")

                return if (kClass.isAbstract || kClass.isSealed || kClass.java.isInterface) {
                    abstractCache.computeIfAbsent(kClass) {
                        val provider =
                            providersSnapShot[kClass]
                                ?: error("바인딩 누락: $kClass")
                        provider()
                    }
                } else {
                    concreteCache.computeIfAbsent(type) { createByConstructorInjection(kClass) }
                }
            }

            private fun createByConstructorInjection(target: KClass<*>): Any {
                val constructor =
                    target.primaryConstructor
                        ?: target.constructors.maxByOrNull { it.parameters.size }
                        ?: error("생성자 없음: ${target.qualifiedName}")

                val args =
                    constructor.parameters
                        .map { param ->
                            resolve(param.type)
                        }.toTypedArray()

                return constructor.call(*args)
            }
        }
    }
}
