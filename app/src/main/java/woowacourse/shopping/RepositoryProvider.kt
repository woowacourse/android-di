package woowacourse.shopping

import woowacourse.shopping.data.CartRepositoryAnnotation
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductsRepositoryAnnotation
import woowacourse.shopping.data.Repository

object RepositoryProvider {
    val repositories: Map<Annotation, Repository> =
        mapOf(
            ProductsRepositoryAnnotation() to DefaultProductRepository(),
            CartRepositoryAnnotation() to DefaultCartRepository(),
        )
}
