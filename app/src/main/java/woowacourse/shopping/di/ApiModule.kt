package woowacourse.shopping.di

import woowacourse.shopping.data.CartSampleRepository
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ApiModule() : DiContainer() {
    val productRepository: ProductRepository =
        this.createInstance(ProductSampleRepository::class.java)

    val cartRepository: CartRepository =
        this.createInstance(CartSampleRepository::class.java)
}
