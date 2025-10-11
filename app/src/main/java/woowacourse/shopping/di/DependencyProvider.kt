package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.RepositoryModule.cartRepository
import woowacourse.shopping.di.RepositoryModule.productRepository

object DependencyProvider {
    val viewModelFactory: ViewModelProvider.Factory by lazy {
        AutoDIViewModelFactory(
            dependencies =
                mapOf(
                    CartRepository::class to cartRepository,
                    ProductRepository::class to productRepository,
                ),
        )
    }
}
