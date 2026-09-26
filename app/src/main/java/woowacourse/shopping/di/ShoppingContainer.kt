package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import com.cksckckcks.di.DiContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.LocalMemoryCart
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingContainer(
    context: Context,
) : DiContainer() {
    private val database =
        Room
            .databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()

    init {
        register(CartProductDao::class) {
            database.cartProductDao()
        }

        register(CartRepository::class, LocalMemoryCart::class) {
            val dao = getInstance(CartProductDao::class) as CartProductDao
            DefaultCartRepository(dao)
        }

        register(CartRepository::class, InMemoryCart::class) {
            InMemoryCartRepository()
        }
    }
}
