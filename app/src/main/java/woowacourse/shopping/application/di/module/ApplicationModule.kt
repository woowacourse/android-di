package woowacourse.shopping.application.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.local.dao.CartProductDao
import woowacourse.shopping.local.db.ShoppingDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideShoppingDatabase(
        @ApplicationContext context: Context,
    ): ShoppingDatabase {
        return Room.databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            "shopping_database",
        ).build()
    }

    @Provides
    @Singleton
    fun provideCartProductDao(shoppingDatabase: ShoppingDatabase): CartProductDao {
        return shoppingDatabase.cartProductDao()
    }
}
