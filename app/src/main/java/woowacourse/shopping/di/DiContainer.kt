package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object DiContainer {
    private const val ERROR_MESSAGE = "%s 인스턴스가 생성되지 않았습니다"

    private val instancePool: MutableMap<KClass<*>, Any> = mutableMapOf()

    private val implementationMappings: MutableMap<KClass<*>, KClass<*>> = mutableMapOf(
        CartRepository::class to DefaultCartRepository::class,
        ProductRepository::class to DefaultProductRepository::class,
    )

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(kClass: KClass<T>): T {
        return instancePool.getOrPut(kClass) {
            val implClass: KClass<*> = implementationMappings[kClass]
                ?: throw IllegalArgumentException(ERROR_MESSAGE.format(kClass.simpleName))
            implClass.createInstance()
        } as T
    }
}
