package woowacourse.shopping.di

import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import com.angrypig.autodi.autoDIModule.autoDIModule

val repositoryModule = com.angrypig.autodi.autoDIModule.autoDIModule("repository") {
    singleton<CartRepository>("singleton") { CartRepositoryImpl() }
    singleton<ProductRepository>("singleton") { ProductRepositoryImpl() }
}
