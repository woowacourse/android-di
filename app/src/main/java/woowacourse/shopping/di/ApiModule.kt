package woowacourse.shopping.di

import woowacourse.shopping.data.CartSampleRepository
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel

class ApiModule() : DiContainer() {

    fun provideProductViewModel(): MainViewModel =
        this.createInstance(MainViewModel::class)

    fun provideProductRepository(): ProductRepository =
        this.createInstance(ProductSampleRepository::class)

    fun provideCartRepository(): CartRepository =
        this.createInstance(CartSampleRepository::class)
}
