package woowacourse.shopping

import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.di.DIApplication
import woowacourse.shopping.di.RepositoryContainer
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : DIApplication() {

    override fun inject() {
        repositoryContainer = RepositoryContainer.of(
            CartRepository::class to CartDefaultRepository(),
            ProductRepository::class to ProductDefaultRepository(),
        )
    }
}
