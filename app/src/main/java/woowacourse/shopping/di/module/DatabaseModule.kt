package woowacourse.shopping.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.qualifier.InMemoryDatabase
import woowacourse.shopping.di.qualifier.RoomDatabase
import javax.inject.Singleton

private const val DATABASE_NAME = "shopping_db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    @RoomDatabase
    fun provideRoom(
        @ApplicationContext context: Context,
    ): ShoppingDatabase =
        Room
            .databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                DATABASE_NAME,
            ).build()

    @Provides
    @Singleton
    @InMemoryDatabase
    fun provideInMemory(
        @ApplicationContext context: Context,
    ): ShoppingDatabase = Room.inMemoryDatabaseBuilder(context, ShoppingDatabase::class.java).build()

    @Provides
    @RoomDatabase
    fun provideRoomCartProductDao(
        @RoomDatabase database: ShoppingDatabase,
    ): CartProductDao = database.cartProductDao()

    @Provides
    @InMemoryDatabase
    fun provideInMemoryCartProductDao(
        @InMemoryDatabase database: ShoppingDatabase,
    ): CartProductDao = database.cartProductDao()
}
