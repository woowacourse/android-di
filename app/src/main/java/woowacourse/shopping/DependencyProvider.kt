package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KType
import kotlin.reflect.typeOf

object DependencyProvider {
    val Dependencies: Map<KType, Any> =
        mapOf(
            typeOf<ProductRepository>() to DefaultProductRepository(),
            typeOf<CartRepository>() to DefaultCartRepository(),
        )
}
