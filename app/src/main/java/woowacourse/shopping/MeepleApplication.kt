package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.example.di.AppContainer
import com.example.di.Scope
import com.example.di.ViewModelFactory
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

class MeepleApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    lateinit var viewModelFactory: ViewModelFactory
        private set

    private lateinit var shoppingDatabase: ShoppingDatabase

    override fun onCreate() {
        super.onCreate()

        shoppingDatabase =
            Room
                .databaseBuilder(
                    this,
                    ShoppingDatabase::class.java,
                    "shopping_db",
                ).build()

        appContainer =
            AppContainer().apply {
                register(CartRepository::class, Scope.Singleton) {
                    DefaultCartRepository(shoppingDatabase.cartProductDao())
                }

                register(ProductRepository::class, Scope.Singleton) {
                    DefaultProductRepository()
                }

                register(DateFormatter::class, Scope.Activity) {
                    DateFormatter(this@MeepleApplication)
                }
            }

        viewModelFactory = ViewModelFactory(appContainer)
    }
}
