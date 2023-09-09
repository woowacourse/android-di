package woowacourse.shopping.di

import woowacourse.shopping.data.CartInDiskRepository
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DiApplicationModule : DiContainer() {

    val provideProductRepository: ProductRepository by lazy {
        this.createInstance(ProductSampleRepository::class)
    }

    val provideCartRepositoryInMemory: CartRepository by lazy {
        this.createInstance(CartInMemoryRepository::class)
    }

    val provideCartRepositoryInDisk: CartRepository by lazy {
        this.createInstance(CartInDiskRepository::class)
    }
}
