package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

fun installAllBindings(builder: ContainerBuilder) {
    builder.register(ProductRepository::class) { ProductRepositoryImpl() }
    builder.register(CartRepository::class) { CartRepositoryImpl() }
}
