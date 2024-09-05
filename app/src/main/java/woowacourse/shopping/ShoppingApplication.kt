package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.RepositoryContainer
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injectRepository()
    }

    private fun injectRepository() {
        RepositoryContainer.setInstance(
            ProductRepository::class,
            DefaultProductRepository(),
        )
        RepositoryContainer.setInstance(
            CartRepository::class,
            DefaultCartRepository(),
        )
    }
}
