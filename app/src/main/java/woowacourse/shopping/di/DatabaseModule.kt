package woowacourse.shopping.di

import InMemory
import Local
import android.content.Context
import androidx.room.Room
import com.yrsel.di.Module
import com.yrsel.di.annotation.Provides
import com.yrsel.di.annotation.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class DatabaseModule(
    private val context: Context,
) : Module {
    @Provides
    @Singleton
    @Local
    fun provideRoomDatabase(): ShoppingDatabase = Room.databaseBuilder(context, ShoppingDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    @InMemory
    fun provideInMemoryDatabase(): ShoppingDatabase = Room.inMemoryDatabaseBuilder(context, ShoppingDatabase::class.java).build()

    @Provides
    @Singleton
    @Local
    fun provideLocalCartProductDao(
        @Local shoppingDatabase: ShoppingDatabase,
    ): CartProductDao = shoppingDatabase.cartProductDao()

    @Provides
    @Singleton
    @InMemory
    fun provideInMemoryCartProductDao(
        @InMemory shoppingDatabase: ShoppingDatabase,
    ): CartProductDao = shoppingDatabase.cartProductDao()

    companion object {
        private const val DATABASE_NAME = "shopping_db"
    }
}
