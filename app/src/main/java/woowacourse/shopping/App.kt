package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DefaultAppContainer
import woowacourse.shopping.di.RepositoryModule.cartRepository
import woowacourse.shopping.di.RepositoryModule.productRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        bind()
    }

    private fun bind() {
        DefaultAppContainer.bind(ProductRepository::class, productRepository)
        DefaultAppContainer.bind(CartRepository::class, cartRepository)
    }
}
