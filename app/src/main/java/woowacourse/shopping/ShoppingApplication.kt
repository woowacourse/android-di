package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import org.aprilgom.androiddi.diContainer
import org.aprilgom.androiddi.factory
import org.aprilgom.androiddi.module
import org.aprilgom.androiddi.modules
import org.aprilgom.androiddi.single
import org.aprilgom.androiddi.viewModel
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val db =
            Room.inMemoryDatabaseBuilder(this@ShoppingApplication, ShoppingDatabase::class.java)
                .build()
        val diModule = module {
            factory(named = "QDBInmemoryDao") { db.cartProductDao() }
            factory(named = "QProductRepository") { ProductRepository() }
            factory(named = "QDefaultCartRepository") { DefaultCartRepository() }
        }
        diContainer {
            modules(diModule)
        }

        /*
        val diModule = module {
            context(this@MainActivity)
            viewModel { MainViewModel() }
        }
        diContainer {
            context(this@MainActivity)
            modules(diModule)
        }
         */
    }
}
