package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DiApplicationModule(
    private val applicationContext: Context,
) : DiContainer() {
    fun provideApplicationContext(): Context = applicationContext

    fun provideProductRepository(): ProductRepository = ProductSampleRepository()

    fun provideCartRepositoryInMemory(): CartRepository = CartInMemoryRepository()

    fun provideShoppingDatabase(
        context: Context,
    ): ShoppingDatabase = ShoppingDatabase.getInstance(context)
}
