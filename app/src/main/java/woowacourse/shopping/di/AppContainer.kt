package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.di.bind
import woowacourse.di.diContainer
import woowacourse.di.register
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.RoomCart
import woowacourse.shopping.data.ShoppingDatabase

object AppContainer {
    lateinit var viewModelFactory: DIViewModelFactory
        private set

    fun initialize(context: Context) {
        val database =
            Room
                .databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()
        val container =
            diContainer {
                register<CartProductDao>(database.cartProductDao())
                bind<CartRepository, DefaultCartRepository>(RoomCart::class)
                bind<CartRepository, InMemoryCartRepository>(InMemoryCart::class)
            }
        viewModelFactory = DIViewModelFactory(container)
    }
}
