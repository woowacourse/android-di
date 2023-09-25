package woowacourse.shopping.di

import woowacourse.shopping.Qualifier
import woowacourse.shopping.Singleton
import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.PersistentCartRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DependencyProvider {
    /*@Singleton
    fun provideCartProductDao(context: Context): CartProductDao {
        return ShoppingDatabase.getDatabase(context).cartProductDao()
    }*/

    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }

    @Singleton
    @Qualifier("persistence")
    fun provideCartRepository(cartProductDao: CartProductDao): CartRepository {
        return PersistentCartRepository(cartProductDao)
    }
}
