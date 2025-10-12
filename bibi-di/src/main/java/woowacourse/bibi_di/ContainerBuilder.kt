package woowacourse.bibi_di

import kotlin.concurrent.getOrSet
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

class ContainerBuilder {
    private val providers = mutableMapOf<Key, () -> Any>()

    fun <A : Any> register(
        abstractType: KClass<A>,
        provider: () -> A,
    ): ContainerBuilder =
        apply {
            providers[Key(abstractType, null)] = provider as () -> Any
        }

    fun <A : Any> register(
        abstractType: KClass<A>,
        qualifier: KClass<out Annotation>,
        provider: () -> A,
    ): ContainerBuilder = apply { providers[Key(abstractType, qualifier)] = provider as () -> Any }

    fun build(): AppContainer {
        val providersSnap = providers.toMap()

        return object : AppContainer {
            private val cache = mutableMapOf<Key, Any>()
            private val resolving = ThreadLocal<MutableSet<KType>>()

            override fun resolve(
                type: KType,
                qualifier: KClass<out Annotation>?,
            ): Any {
                val kClass =
                    type.classifier as? KClass<*>
                        ?: error("지원하지 않는 타입: $type")

                return if (kClass.isAbstract || kClass.isSealed || kClass.java.isInterface) {
                    val key = Key(kClass, qualifier)
                    cache.computeIfAbsent(key) {
                        val provider =
                            providersSnap[key]
                                ?: error("바인딩 누락: $kClass (qualifier = ${qualifier?.simpleName})")
                        provider()
                    }
                } else {
                    val seen = resolving.getOrSet { mutableSetOf() }
                    seen.withElement(type) { createByConstructorInjection(kClass) }
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

private data class Key(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>?,
)
