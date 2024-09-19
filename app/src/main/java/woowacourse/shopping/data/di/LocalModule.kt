package woowacourse.shopping.data.di

import android.content.Context
import com.android.di.component.DiSingletonComponent
import woowacourse.shopping.data.createInMemoryDatabase
import woowacourse.shopping.data.createRoomDatabase
import woowacourse.shopping.data.di.annotation.InMemoryDatabase
import woowacourse.shopping.data.di.annotation.RoomDatabase

object LocalModule {
    fun install(context: Context) {
        provideShoppingDatabase(context)
        provideInMemoryShoppingDatabase(context)
    }

    private fun provideShoppingDatabase(context: Context) {
        val database = createRoomDatabase(context)
        DiSingletonComponent.provide(
            RoomDatabase::class,
            database.cartProductDao()
        )
    }

    private fun provideInMemoryShoppingDatabase(context: Context) {
        val database = createInMemoryDatabase(context)
        DiSingletonComponent.provide(
            InMemoryDatabase::class,
            database.cartProductDao()
        )
    }
}
