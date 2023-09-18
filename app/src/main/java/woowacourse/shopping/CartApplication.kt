package woowacourse.shopping

import com.dygames.android_di.lifecycle.LifecycleWatcherApplication
import com.dygames.di.dependencies
import com.dygames.di.lifecycle
import com.dygames.di.provider
import com.dygames.di.qualifier
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.DateFormatter
import kotlin.reflect.typeOf

class CartApplication : LifecycleWatcherApplication(typeOf<CartApplication>()) {
    override fun onCreate() {
        initDependencies()
        super.onCreate()
    }

    private fun initDependencies() {
        dependencies {
            lifecycle<CartApplication> {
                qualifier(Room()) {
                    val shoppingDatabase: ShoppingDatabase by lazy {
                        ShoppingDatabase.getDatabase(
                            this@CartApplication
                        )
                    }
                    provider { shoppingDatabase.cartProductDao() }
                    provider<CartRepository>(typeOf<DefaultCartRepository>())
                }
                provider<ProductRepository> { DefaultProductRepository() }
            }
            lifecycle<CartActivity> {
                provider<DateFormatter>(typeOf<DateFormatter>())
            }
        }
    }
}
