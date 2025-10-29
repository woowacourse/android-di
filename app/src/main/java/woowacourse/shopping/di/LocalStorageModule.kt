package woowacourse.shopping.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {
    @Provides
    @Singleton
    fun shoppingDatabase(
        @ApplicationContext context: Context,
    ) = ShoppingDatabase.getDataBase(context)

    @Provides
    @Singleton
    fun cartProductDao(database: ShoppingDatabase): CartProductDao = database.cartProductDao()
}
