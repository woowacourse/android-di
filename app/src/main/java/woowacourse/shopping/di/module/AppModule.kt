package woowacourse.shopping.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.cart.CartProductDao
import woowacourse.shopping.data.cart.DefaultCartRepository
import woowacourse.shopping.data.cart.InMemoryCartRepository
import woowacourse.shopping.di.qualifire.DataBase
import woowacourse.shopping.di.qualifire.InMemory
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()

    @Provides
    fun provideCartProductDao(@ApplicationContext context: Context): CartProductDao =
        ShoppingDatabase.getDatabase(context).cartProductDao()

    @DataBase
    @Provides
    fun provideDatabaseCartRepository(cartProductDao: CartProductDao): CartRepository =
        DefaultCartRepository(cartProductDao)

    @InMemory
    @Provides
    fun provideInMemoryCartRepository(): CartRepository {
        return InMemoryCartRepository()
    }
}
