package woowacourse.shopping.hilt.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideShoppingDatabase(
        @ApplicationContext context: Context,
    ): ShoppingDatabase = ShoppingDatabase.getInstance(context)

    @Provides
    fun provideCartProductDao(database: ShoppingDatabase): CartProductDao = database.cartProductDao()
}
