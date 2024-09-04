package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

class DefaultAppModule(private val appContext: Context) : AppModule {
    override val productRepository: ProductRepository = ProductRepositoryImpl()

    override val cartRepository: CartRepository = CartRepositoryImpl()
}
