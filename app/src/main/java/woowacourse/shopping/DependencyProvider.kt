package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass

object DependencyProvider {
    val Dependencies: Map<KClass<*>, Any> =
        mapOf(
            ProductRepository::class to DefaultProductRepository(),
            CartRepository::class to DefaultCartRepository(),
        )
}
