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

        DependencyContainer.openScope(APP_SCOPE_ID)

        DependencyContainer.register(
            APP_SCOPE_ID,
            Context::class,
            applicationContext,
        )

        DependencyContainer.register(
            APP_SCOPE_ID,
            CartProductDao::class,
            dao,
        )

        DependencyContainer.register(
            APP_SCOPE_ID,
            CartRepository::class,
            woowacourse.shopping.di.qualifier.Room::class,
            DefaultCartRepository(dao),
        )

        DependencyContainer.register(
            APP_SCOPE_ID,
            CartRepository::class,
            InMemory::class,
            InMemoryCartRepository(),
        )
    }

    companion object {
        const val APP_SCOPE_ID = 1L
    }
}
