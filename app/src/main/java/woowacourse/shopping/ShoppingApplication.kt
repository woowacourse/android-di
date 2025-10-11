package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRoomRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import woowacourse.shopping.di.DIContainer

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
            ProductDefaultRepository::class,
            CartRoomRepository::class,
        ).registerSingleton(database)
    }
}
