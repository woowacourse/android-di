package woowacourse.shopping

import android.app.Application
import com.boogiwoogi.di.Dependency
import com.boogiwoogi.di.WoogiInjector
import com.boogiwoogi.di.woogiInitializer
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupDependencyInjector()
    }

    private fun setupDependencyInjector() {
        val context = this
        injector = woogiInitializer {
            declareDependency(
                Dependency<CartRepository>(
                    DatabaseCartRepository(
                        ShoppingDatabase.getDatabase(context).cartProductDao()
                    )
                )
            )
            declareDependency(Dependency<CartRepository>(InMemoryCartRepository()))
            declareDependency(Dependency<ProductRepository>(DefaultProductRepository()))
        }
    }

    companion object {

        lateinit var injector: WoogiInjector
    }
}
