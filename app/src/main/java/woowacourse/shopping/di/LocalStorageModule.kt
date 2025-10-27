package woowacourse.shopping.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {
    @Provides
    fun shoppingDatabase(context: Context) = ShoppingDatabase.getDataBase(context)

    @Provides
    fun cartProductDao(database: ShoppingDatabase): CartProductDao = database.cartProductDao()
}
