package woowacourse.shopping.ui.di

import android.content.Context
import androidx.room.Room
import io.hyemdooly.di.Module
import io.hyemdooly.di.annotation.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.InDiskCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase

class ApplicationModule(private val applicationContext: Context) : Module() {
    @Singleton
    fun provideContext() = applicationContext

    @Singleton
    fun provideCartProductDao() = Room.databaseBuilder(
        applicationContext,
        ShoppingDatabase::class.java,
        ShoppingDatabase.name,
    ).build().cartProductDao()

    @Singleton
    fun provideInMemoryCartRepository() = InMemoryCartRepository()

    @Singleton
    fun provideInDiskCartRepository(dao: CartProductDao) = InDiskCartRepository(dao)
}
