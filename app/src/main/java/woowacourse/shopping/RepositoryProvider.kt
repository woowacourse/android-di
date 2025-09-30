package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.Repository
import kotlin.reflect.KClass

object RepositoryProvider {
    val Repositories: Map<KClass<out Repository>, Repository> =
        mapOf(
            ProductRepository::class to DefaultProductRepository(),
            CartRepository::class to DefaultCartRepository(),
        )
}
