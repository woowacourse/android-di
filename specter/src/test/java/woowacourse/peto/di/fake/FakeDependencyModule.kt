package woowacourse.peto.di.fake

import woowacourse.peto.di.DependencyModule
import woowacourse.peto.di.annotation.Qualifier
import woowacourse.peto.di.fake.repository.cart.CartRepository
import woowacourse.peto.di.fake.repository.cart.DefaultCartRepository
import woowacourse.peto.di.fake.repository.product.DefaultProductRepository
import woowacourse.peto.di.fake.repository.product.ProductRepository

class FakeDependencyModule : DependencyModule {
    val stringProvider: String = "inject"

    @Qualifier("myCart")
    val myCartRepository: CartRepository = DefaultCartRepository()

    @Qualifier("othersCart")
    val otherCartRepository: CartRepository = DefaultCartRepository()

    val productRepository: ProductRepository = DefaultProductRepository()
}
