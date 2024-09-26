package woowacourse.shopping.hilt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.hilt.data.CartProductDao
import woowacourse.shopping.hilt.data.ShoppingDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoBaseModule {

    @Provides
    @Singleton
    fun provideCartProductDao(database: ShoppingDatabase): CartProductDao =
        database.cartProductDao()
}
