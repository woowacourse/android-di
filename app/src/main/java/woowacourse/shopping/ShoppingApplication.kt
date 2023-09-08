package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Dependency
import woowacourse.shopping.di.Dependencies
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        container = Dependencies(
            listOf(
                Dependency<CartRepository>(
                    DefaultCartRepository(
                        ShoppingDatabase.getDatabase(this).cartProductDao()
                    )
                ),
                Dependency<ProductRepository>(DefaultProductRepository())
            )
        )
    }

    companion object {

        lateinit var container: Dependencies
    }
}
