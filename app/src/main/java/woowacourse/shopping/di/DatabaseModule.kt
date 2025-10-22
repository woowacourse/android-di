package woowacourse.shopping.di

import InMemory
import LocalDatabase
import android.content.Context
import androidx.room.Room
import com.yrsel.di.Module
import com.yrsel.di.annotation.Provides
import com.yrsel.di.annotation.SingletonScope
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class DatabaseModule(
    private val context: Context,
) : Module {
    @Provides
    @SingletonScope
    @LocalDatabase
    fun provideRoomDatabase(): ShoppingDatabase = Room.databaseBuilder(context, ShoppingDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @SingletonScope
    @InMemory
    fun provideInMemoryDatabase(): ShoppingDatabase = Room.inMemoryDatabaseBuilder(context, ShoppingDatabase::class.java).build()

    @Provides
    @LocalDatabase
    fun provideLocalCartProductDao(
        @LocalDatabase shoppingDatabase: ShoppingDatabase,
    ): CartProductDao = shoppingDatabase.cartProductDao()

    @Provides
    @InMemory
    fun provideInMemoryCartProductDao(
        @InMemory shoppingDatabase: ShoppingDatabase,
    ): CartProductDao = shoppingDatabase.cartProductDao()

    companion object {
        private const val DATABASE_NAME = "shopping_db"
    }
}
