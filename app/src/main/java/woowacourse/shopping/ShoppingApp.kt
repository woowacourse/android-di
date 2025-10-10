package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DependencyInjectorImpl
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApp : Application() {
    private val database: ShoppingDatabase by lazy {
        ShoppingDatabase.getInstance(this)
    }

    override fun onCreate() {
        super.onCreate()
        DependencyInjectorImpl.setInstance(ProductRepository::class, ProductRepositoryImpl())
        DependencyInjectorImpl.setInstance(
            CartRepository::class,
            CartRepositoryImpl(database.cartProductDao()),
        )
    }
}
