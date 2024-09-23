package woowacourse.shopping

import com.example.di.DependencyModule
import com.example.yennydi.application.DiApplication
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DataBaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingApplication() : DiApplication() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun setupDependency() {
        val db = ShoppingDatabase.getInstance(this)

        DependencyModule.apply {
            addDeferredDependency(
                ProductRepository::class to ProductRepositoryImpl::class,
                DateFormatter::class to DateFormatter::class,
            )

            addSingletonDependency(
                CartRepository::class to DataBaseCartRepository::class,
                CartRepository::class to InMemoryCartRepository::class,
            )

            addInstanceDependency(
                CartProductDao::class to db.cartProductDao(),
            )
        }
    }
}
