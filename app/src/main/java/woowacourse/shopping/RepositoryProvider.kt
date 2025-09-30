package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.Repository

object RepositoryProvider {
    val repositories: Map<Class<out Repository>, Repository> =
        mapOf(
            ProductRepository::class.java to DefaultProductRepository(),
            CartRepository::class.java to DefaultCartRepository(),
        )
}
