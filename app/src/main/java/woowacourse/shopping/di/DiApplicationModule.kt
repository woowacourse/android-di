package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DiApplicationModule(
    applicationContext: Context,
) : DiContainer() {

    val provideProductRepository: ProductRepository by lazy {
        this.createInstance(ProductSampleRepository::class)
    }

    val provideCartRepositoryInMemory: CartRepository by lazy {
        this.createInstance(CartInMemoryRepository::class)
    }

    val provideShoppingDatabase: ShoppingDatabase by lazy {
        ShoppingDatabase.getInstance(applicationContext)
    }
}
