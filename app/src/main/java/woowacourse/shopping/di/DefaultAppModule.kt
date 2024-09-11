package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DBCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase

class DefaultAppModule(private val appContext: Context) : AppModule {
    override fun provideShoppingDatabase(): ShoppingDatabase =
        ShoppingDatabase.getInstance(appContext)

    override fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    @DatabaseRepository
    override fun provideCartRepository(
        shoppingDatabase: ShoppingDatabase,
    ): CartRepository =
        DBCartRepository(shoppingDatabase.cartProductDao())

    @InMemoryRepository
    override fun provideInMemoryCartRepository(): CartRepository =
        InMemoryCartRepository()
}
