package woowacourse.shopping.di

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
object DatabaseBinder {
    @Database
    fun provideDataBase(
        @ApplicationContext context: Context,
    ): ShoppingDatabase =
        Room.databaseBuilder(context, ShoppingDatabase::class.java, "shopping").build()

    @InMemory
    fun provideInMemoryDataBase(
        @ApplicationContext context: Context,
    ): ShoppingDatabase =
        Room.inMemoryDatabaseBuilder(context, ShoppingDatabase::class.java).build()

}
