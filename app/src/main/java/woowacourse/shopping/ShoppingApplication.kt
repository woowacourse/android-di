package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.DIModule
import woowacourse.shopping.di.Module
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class ShoppingApplication : Application(), DIModule {
    private val shoppingDatabase: ShoppingDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "shopping-database"
        ).build()
    }
    val diContainer: DIContainer by lazy { DIContainer(this) }

    override fun singletonInstance(): List<Module> {
        val dao = shoppingDatabase.cartProductDao()
        return listOf(
            Module(CartRepository::class, DefaultCartRepository(dao)),
            Module(ProductRepository::class, DefaultProductRepository()),
        )
    }
}
