package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.medandro.di.DIContainer
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartInMemoryRepository
import woowacourse.shopping.data.repository.CartRoomRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingApplication : Application() {
    private val database by lazy {
        Room
            .databaseBuilder(
                applicationContext,
                ShoppingDatabase::class.java,
                "shopping_database",
            ).build()
    }

    val diContainer by lazy {
        DIContainer(
            applicationContext,
            ProductDefaultRepository::class,
            CartRoomRepository::class,
            CartInMemoryRepository::class,
            DateFormatter::class,
        ).registerSingleton(database).registerSingleton(database.cartProductDao())
    }
}
