package woowacourse.shopping

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.Repository
import woowacourse.shopping.data.annotations.CartRepositoryAnnotation
import woowacourse.shopping.data.annotations.ProductRepositoryAnnotation

object RepositoryProvider {
    val repositories: Map<Annotation, Repository> =
        mapOf(
            ProductRepositoryAnnotation() to DefaultProductRepository(),
            CartRepositoryAnnotation() to DefaultCartRepository(),
        )
}
