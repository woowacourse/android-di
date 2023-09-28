package woowacourse.shopping.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.cart.CartProductDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CartDaoModule {

    @Provides
    @Singleton
    fun provideCartProductDao(@ApplicationContext context: Context): CartProductDao =
        ShoppingDatabase.getDatabase(context).cartProductDao()
}
