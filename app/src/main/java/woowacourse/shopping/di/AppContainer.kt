package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
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
            DIContainer().apply {
                register(CartProductDao::class, database.cartProductDao())
                bind(CartRepository::class, DefaultCartRepository::class)
            }
        viewModelFactory = DIViewModelFactory(container)
    }
}
