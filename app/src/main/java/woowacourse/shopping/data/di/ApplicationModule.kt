package woowacourse.shopping.data.di

import android.content.Context
import com.android.di.component.DiContainer
import com.android.di.component.Module
import woowacourse.shopping.data.createInMemoryDatabase
import woowacourse.shopping.data.createRoomDatabase
import woowacourse.shopping.data.di.annotation.InMemoryDatabase
import woowacourse.shopping.data.di.annotation.RoomDatabase

class ApplicationModule(private val context: Context) : Module {
    fun provideShoppingDatabase(diContainer: DiContainer) {
        val database = createRoomDatabase(context)
        diContainer.provide(
            RoomDatabase::class,
            database.cartProductDao(),
        )
    }

    fun provideInMemoryShoppingDatabase(diContainer: DiContainer) {
        val database = createInMemoryDatabase(context)
        diContainer.provide(
            InMemoryDatabase::class,
            database.cartProductDao(),
        )
    }
}
