package woowacourse.shopping.di.container

import woowacourse.shopping.data.SampleCartRepository
import woowacourse.shopping.data.SampleProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class SampleContainer : ShoppingContainer {
    override val productRepository: ProductRepository = SampleProductRepository()
    override val cartRepository: CartRepository = SampleCartRepository()
}
