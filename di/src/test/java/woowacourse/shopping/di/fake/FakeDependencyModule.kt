package woowacourse.shopping.di.fake

import woowacourse.shopping.di.DependencyModule
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.di.fake.repository.cart.CartRepository
import woowacourse.shopping.di.fake.repository.cart.DefaultCartRepository
import woowacourse.shopping.di.fake.repository.product.DefaultProductRepository
import woowacourse.shopping.di.fake.repository.product.ProductRepository

class FakeDependencyModule : DependencyModule {
    val stringProvider: String = "inject"

    @Qualifier("myCart")
    val myCartRepository: CartRepository = DefaultCartRepository()

    @Qualifier("othersCart")
    val otherCartRepository: CartRepository = DefaultCartRepository()

    val productRepository: ProductRepository = DefaultProductRepository()
}
