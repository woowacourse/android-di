package woowacourse.shopping.di

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
        return object : AppContainer {
            private val cache = mutableMapOf<KType, Any>()

            override fun resolve(type: KType): Any {
                cache[type]?.let { return it }

                val kClass =
                    type.classifier as? KClass<*>
                        ?: error("지원하지 않는 타입: $type")

                val instance: Any =
                    if (kClass.isAbstract || kClass.isSealed || kClass.java.isInterface) {
                        val provider = providers[kClass] ?: error("바인딩 누락: $kClass")
                        provider()
                    } else {
                        createByConstructorInjection(kClass)
                    }
                return instance.also { cache[type] = it }
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
