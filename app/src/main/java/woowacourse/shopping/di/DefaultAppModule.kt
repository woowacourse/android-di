package woowacourse.shopping.di

import android.content.Context
import com.kmlibs.supplin.annotations.ApplicationContext
import com.kmlibs.supplin.annotations.Module
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DBCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase

@Module
object DefaultAppModule {
    fun provideShoppingDatabase(
        @ApplicationContext applicationContext: Context,
    ): ShoppingDatabase = ShoppingDatabase.getInstance(applicationContext)

    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    @DatabaseRepository
    fun provideCartRepository(shoppingDatabase: ShoppingDatabase): CartRepository = DBCartRepository(shoppingDatabase.cartProductDao())

    @InMemoryRepository
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()
}
