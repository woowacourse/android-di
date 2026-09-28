package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository_impl.DefaultCartRepository
import kotlin.reflect.KClass

class MyApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(applicationContext)
    }
}

class AppContainer(context: Context) {
    val database: ShoppingDatabase = Room.databaseBuilder<ShoppingDatabase>(context, "db_name")
        .setDriver(AndroidSQLiteDriver())
        .build()
    val cartProductDao: CartProductDao = database.cartProductDao()

    val bindings: Map<KClass<*>, KClass<*>> = mapOf(CartRepository::class to DefaultCartRepository::class)

    val productRepository: ProductRepository = ProductRepository()
}
