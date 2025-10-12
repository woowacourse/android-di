package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.di.auto.Container
import woowacourse.di.auto.Database
import woowacourse.di.auto.InMemory
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase

object AppContainer : Container() {
    fun init(context: Context) {
        bindSingleton(Context::class) { context.applicationContext }

        bindSingleton(ShoppingDatabase::class) {
            Room
                .databaseBuilder(
                    get(Context::class),
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()
        }

        bindSingleton(CartProductDao::class) {
            get(ShoppingDatabase::class).cartProductDao()
        }

        bindSingleton(CartRepository::class, qualifier = Database::class) {
            RoomCartRepository(get(CartProductDao::class))
        }

        bindSingleton(CartRepository::class, qualifier = InMemory::class) {
            InMemoryCartRepository()
        }

        bindSingleton(CartRepository::class) {
            get(CartRepository::class)
        }

        bindSingleton(ProductRepository::class) {
            DefaultProductRepository()
        }
    }
}
