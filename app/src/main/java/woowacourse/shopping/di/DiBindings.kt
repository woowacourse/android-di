package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

fun installAllBindings(container: ShoppingAppContainer) {
    container.register(ProductRepository::class) { ProductRepositoryImpl() }
    container.register(CartRepository::class) { CartRepositoryImpl() }
}
