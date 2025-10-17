package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.example.di.AppContainer
import com.example.di.InMemory
import com.example.di.RoomDatabase
import com.example.di.ViewModelFactory
import com.example.di.container.DIKey
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
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
            AppContainer(
                bindings =
                    mapOf(
                        DIKey(CartRepository::class, RoomDatabase::class) to {
                            DefaultCartRepository(shoppingDatabase.cartProductDao())
                        },
                        DIKey(ProductRepository::class, InMemory::class) to {
                            DefaultProductRepository()
                        },
                        DIKey(DateFormatter::class) to { DateFormatter(this) },
                    ),
            )

        viewModelFactory = ViewModelFactory(appContainer)
    }
}
