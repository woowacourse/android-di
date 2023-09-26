package woowacourse.shopping.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CartProductDaoModule {

    @Singleton
    @Provides
    fun provideCartProductDao(
        @ApplicationContext
        context: Context
    ): CartProductDao {
        return ShoppingDatabase.getDatabase(context).cartProductDao()
    }
}
