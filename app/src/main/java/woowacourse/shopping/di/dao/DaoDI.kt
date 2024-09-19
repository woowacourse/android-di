package woowacourse.shopping.di.dao

import android.content.Context
import androidx.room.Room
import com.woowa.di.ApplicationContext
import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import javax.inject.Qualifier

@Qualifier
annotation class InMemory

@Qualifier
annotation class Database

@InstallIn(SingletonComponent::class)
class DaoBinder {
    @Database
    fun provideCartProductDao(
        @ApplicationContext context: Context,
    ): CartProductDao = provideShoppingDataBase(context).cartProductDao()

    @InMemory
    fun provideInMemoryCartProductDao(
        @ApplicationContext context: Context,
    ): CartProductDao = provideShoppingInMemoryDataBase(context).cartProductDao()

    private fun provideShoppingDataBase(context: Context): ShoppingDatabase =
        Room.databaseBuilder(context, ShoppingDatabase::class.java, "shopping").build()

    private fun provideShoppingInMemoryDataBase(context: Context): ShoppingDatabase =
        Room.inMemoryDatabaseBuilder(context, ShoppingDatabase::class.java).build()
}
