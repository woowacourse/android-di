package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.di.annotations.StorageType
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DefaultModule(private val context: Context) : Module {

    fun provideCartProductDao(): CartProductDao {
        val db = ShoppingDatabase.getInstance(context.applicationContext)
        return db.cartProductDao()
    }

    fun provideProductRepository(): ProductRepository = DefaultProductRepository()

    @Qualifier(StorageType.DATABASE)
    fun provideDatabaseCartRepository(cartProductDao: CartProductDao): CartRepository =
        DefaultCartRepository(cartProductDao)

    @Qualifier(StorageType.IN_MEMORY)
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()
}