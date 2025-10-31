package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.ShoppingDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context,
    ) = Room
        .databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            "shopping_db",
        ).build()

    @Singleton
    @Provides
    fun provideCartProductDao(shoppingDatabase: ShoppingDatabase) = shoppingDatabase.cartProductDao()
}
