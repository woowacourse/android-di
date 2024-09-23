package woowacourse.shopping.data.di

import android.content.Context
import com.woowacourse.di.DiContainer
import com.woowacourse.di.DiModule
import woowacourse.shopping.data.InMemoryCartProductDao
import woowacourse.shopping.data.createRoomDatabase
import woowacourse.shopping.shoppingapp.di.annotation.InMemoryDatabase
import woowacourse.shopping.shoppingapp.di.annotation.RoomDatabase

class DatabaseModule(private val context: Context) : DiModule {
    fun provideShoppingDatabase(diContainer: DiContainer) {
        val database = createRoomDatabase(context)
        diContainer.provide(RoomDatabase::class, database.cartProductDao())
    }

    fun provideInMemoryShoppingDatabase(diContainer: DiContainer) {
        val database = InMemoryCartProductDao()
        diContainer.provide(InMemoryDatabase::class, database)
    }
}
