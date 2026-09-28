package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.di.SamDi
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository_impl.DefaultCartRepository
import woowacourse.shopping.data.repository_impl.FakeCartRepository
import woowacourse.shopping.util.annotations.InMemoryRepo
import woowacourse.shopping.util.annotations.RoomRepo
import kotlin.reflect.KClass

class MyApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(applicationContext)
    }
}

class AppContainer(
    context: Context,
) {
    val database: ShoppingDatabase =
        Room
            .databaseBuilder<ShoppingDatabase>(context, "db_name")
            .setDriver(AndroidSQLiteDriver())
            .build()
    val cartProductDao: CartProductDao = database.cartProductDao()

    val bindings: Map<Pair<KClass<*>, KClass<*>>, KClass<*>> =
        mapOf(
            Pair(
                CartRepository::class,
                RoomRepo::class,
            ) to DefaultCartRepository::class,
            Pair(
                CartRepository::class,
                InMemoryRepo::class,
            ) to FakeCartRepository::class,
        )

    val productRepository: ProductRepository = ProductRepository()

    val di =
        SamDi(
            providers =
                mapOf(
                    CartProductDao::class to { cartProductDao },
                    ProductRepository::class to { productRepository },
                ),
            bindings = bindings,
        )
}
