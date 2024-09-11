package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import woowacourse.shopping.di.DIContainer.inject

class DefaultAppModule(
    appContext: Context,
) : AppModule {
    val database = ShoppingDatabase.getInstance(appContext)

    override val productRepository: ProductRepository by lazy { inject<ProductRepository>() }

    override val cartRepository: CartRepository by lazy { inject<CartRepository>() }
}
