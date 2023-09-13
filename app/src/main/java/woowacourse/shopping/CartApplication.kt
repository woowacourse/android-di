package woowacourse.shopping

import android.app.Application
import com.dygames.di.dependencies
import com.dygames.di.provider
import com.dygames.di.qualifier
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.Room
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.reflect.typeOf

class CartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initDependencies()
    }

    private fun initDependencies() {
        dependencies {
            qualifier(Room()) {
                val shoppingDatabase: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(this@CartApplication) }
                provider { shoppingDatabase.cartProductDao() }
                provider<CartRepository>(typeOf<DefaultCartRepository>())
            }
            provider<ProductRepository> { DefaultProductRepository() }
        }
    }
}
