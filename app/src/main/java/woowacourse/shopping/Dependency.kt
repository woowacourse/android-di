package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass

object Dependency {
    private val dependencies by lazy {
        mapOf<KClass<*>, Any>(
            ProductRepository::class to DefaultProductRepository(),
            CartRepository::class to DefaultCartRepository(),
        )
    }

    fun get(type: KClass<*>): Any = dependencies[type] ?: throw IllegalArgumentException("No dependency for $type")
}
