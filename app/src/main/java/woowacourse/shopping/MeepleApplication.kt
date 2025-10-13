package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.example.di2.AppContainer
import com.example.di2.DIKey
import com.example.di2.InMemory
import com.example.di2.RoomDatabase
import com.example.di2.ViewModelFactory
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class MeepleApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    lateinit var shoppingDatabase: ShoppingDatabase
        private set

    lateinit var viewModelFactory: ViewModelFactory
        private set

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
                            DefaultCartRepository(
                                shoppingDatabase.cartProductDao(),
                            )
                        },
                        DIKey(
                            ProductRepository::class,
                            InMemory::class,
                        ) to { DefaultProductRepository() },
                    ),
            )

        viewModelFactory = ViewModelFactory(appContainer)
    }
}
