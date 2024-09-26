package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import org.aprilgom.androiddi.diContainer
import org.aprilgom.androiddi.factory
import org.aprilgom.androiddi.get
import org.aprilgom.androiddi.module
import org.aprilgom.androiddi.modules
import org.aprilgom.androiddi.scope
import org.aprilgom.androiddi.scoped
import org.aprilgom.androiddi.single
import org.aprilgom.androiddi.viewModel
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val diModule =
            module {
                single("AndroidContext") { this@ShoppingApplication }
                single(
                    named = "InMemoryDatabase",
                ) {
                    Room.inMemoryDatabaseBuilder(
                        get<Context>("AndroidContext"),
                        ShoppingDatabase::class.java,
                    ).build()
                }
                factory(named = "QDBInMemoryCartDao") { get<ShoppingDatabase>("InMemoryDatabase").cartProductDao() }
                factory(named = "QProductRepository") { ProductRepository() }
                single(named = "QDefaultCartRepository") { DefaultCartRepository() }
                viewModel { MainViewModel() }
                viewModel { CartViewModel() }
                scope(named = "CartActivityScope") {
                    scoped(named = "DateFormatter") { DateFormatter(get("CartActivityScope")) }
                }
            }
        diContainer {
            modules(diModule)
        }
    }
}
