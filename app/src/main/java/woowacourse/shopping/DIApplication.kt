package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.util.autoDI.AutoDI

class DIApplication : Application() {
    override fun onCreate() {
        initAutoDI()
        super.onCreate()
    }

    private fun initAutoDI() {
        AutoDI {
            singleton<CartRepository>("singleton") { CartRepositoryImpl() }
            singleton<ProductRepository>("singleton") { ProductRepositoryImpl() }
            viewModel { MainViewModel(inject("singleton"), inject("singleton")) }
            viewModel { CartViewModel(inject()) }
        }
    }
}
