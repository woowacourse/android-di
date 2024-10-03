package woowacourse.shopping.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.local.CartProductDao
import woowacourse.shopping.local.ShoppingDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideLogDao(database: ShoppingDatabase): CartProductDao {
        return database.cartProductDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ShoppingDatabase {
        return Room.databaseBuilder(
            appContext,
            ShoppingDatabase::class.java,
            "shopping.db"
        ).build()
    }
}