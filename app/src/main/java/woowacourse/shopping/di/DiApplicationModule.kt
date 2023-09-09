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
    fun provideProductRepository(): ProductRepository =
        this.createInstance(ProductSampleRepository::class)

    fun provideCartRepositoryInMemory(): CartRepository =
        this.createInstance(CartInMemoryRepository::class)

    fun provideShoppingDatabase(): ShoppingDatabase =
        ShoppingDatabase.getInstance(applicationContext)
}
