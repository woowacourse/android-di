package woowacourse.shopping.di

import com.angrypig.autodi.autoDIModule.autoDIModule
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

val repositoryModule = autoDIModule("repository") {
    singleton<CartRepository>("singleton") { CartRepositoryImpl(inject()) }
    singleton<ProductRepository>("singleton") { ProductRepositoryImpl() }
    disposable<ProductRepository>("disposable") { ProductRepositoryImpl() }
}
