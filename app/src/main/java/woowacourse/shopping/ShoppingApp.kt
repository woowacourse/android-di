package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowa.shopping.di.libs.android.androidContext
import woowa.shopping.di.libs.android.viewModel
import woowa.shopping.di.libs.container.startDI
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startDI {
            androidContext(this@ShoppingApp)
            container {
                single<ShoppingDatabase> {
                    Room.databaseBuilder(
                        get(),
                        ShoppingDatabase::class.java,
                        ShoppingDatabase.name,
                    ).build()
                }
                single { get<ShoppingDatabase>().cartProductDao() }
            }
            container {
                single<ProductRepository> { ProductRepositoryImpl() }
                single<CartRepository> { CartRepositoryImpl(get()) }
            }
            container {
                viewModel<MainViewModel>()
                viewModel<CartViewModel>()
            }
        }
    }
}