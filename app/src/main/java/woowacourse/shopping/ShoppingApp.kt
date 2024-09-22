package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowa.shopping.di.libs.android.androidContext
import woowa.shopping.di.libs.android.viewModel
import woowa.shopping.di.libs.container.startDI
import woowa.shopping.di.libs.qualify.qualifier
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
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
                        ShoppingDatabase.NAME,
                    ).build()
                }
                single { get<ShoppingDatabase>().cartProductDao() }
            }
            container {
                single<ProductRepository> { ProductRepositoryImpl() }
                single<CartRepository>(qualifier<DefaultCartRepository>()) {
                    DefaultCartRepository(get())
                }
                single<CartRepository>(qualifier<InMemoryCartRepository>()) { InMemoryCartRepository() }
            }
            container {
                viewModel<MainViewModel> {
                    MainViewModel(
                        get(),
                        get(qualifier<DefaultCartRepository>()),
                    )
                }
                viewModel<CartViewModel> { CartViewModel(get(qualifier<DefaultCartRepository>())) }
            }
        }
    }
}
