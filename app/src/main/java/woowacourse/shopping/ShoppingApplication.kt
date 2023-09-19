package woowacourse.shopping

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.DiApplication
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.createDateFormatter

class ShoppingApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()
        setupProviders()
    }

    private fun setupProviders() {
        registerProviders {
            provider(ShoppingDatabase::class to ShoppingDatabase::getInstance)
            provider(CartProductDao::class to ShoppingDatabase::cartProductDao)
            provider(DateFormatter::class to ::createDateFormatter)
        }
    }
}
