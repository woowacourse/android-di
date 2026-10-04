package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import woowacourse.di.DependencyContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.qualifier.InMemory
import woowacourse.shopping.di.qualifier.Room as RoomQualifier

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val database =
            Room
                .databaseBuilder(
                    applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()

        val dao = database.cartProductDao()

        DependencyContainer.register(Context::class, applicationContext)
        DependencyContainer.register(CartProductDao::class, dao)
        DependencyContainer.register(
            CartRepository::class,
            RoomQualifier::class,
            DefaultCartRepository(dao),
        )
        DependencyContainer.register(
            CartRepository::class,
            InMemory::class,
            InMemoryCartRepository(),
        )
    }
}
