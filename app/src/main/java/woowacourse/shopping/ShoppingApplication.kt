package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class ShoppingApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initializeRepository()
    }

    private fun initializeRepository() {
        val cartProductDao: CartProductDao = ShoppingDatabase.initialize(this).cartProductDao()
        productRepository = ProductDefaultRepository()
        cartRepository = CartDefaultRepository(cartProductDao)
    }

    companion object {
        lateinit var productRepository: ProductRepository
            private set

        lateinit var cartRepository: CartRepository
            private set
    }
}
