package woowacourse.shopping

import android.app.Application
import com.example.di.DependencyModule
import com.example.di.Injector
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DataBaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication : Application() {
    lateinit var injector: Injector
        private set

    override fun onCreate() {
        super.onCreate()
        val db = ShoppingDatabase.getInstance(this)

        val dependencyModule =
            DependencyModule().apply {
                addDeferredDependency(
                    CartRepository::class to DataBaseCartRepository::class,
                    CartRepository::class to InMemoryCartRepository::class,
                    ProductRepository::class to ProductRepositoryImpl::class,
                )

                addInstanceDependency(
                    CartProductDao::class to db.cartProductDao(),
                )
            }
        injector = Injector(dependencyModule)
    }
}
