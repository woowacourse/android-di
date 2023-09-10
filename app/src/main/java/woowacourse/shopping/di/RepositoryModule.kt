package woowacourse.shopping.di

import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.util.autoDI.autoDIModule.autoDIModule

val repositoryModule = autoDIModule("repository") {
    singleton<CartRepository>("singleton") { CartRepositoryImpl() }
    singleton<ProductRepository>("singleton") { ProductRepositoryImpl() }
}
