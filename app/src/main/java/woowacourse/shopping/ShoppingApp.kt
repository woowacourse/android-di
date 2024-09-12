package woowacourse.shopping

import android.app.Application
import android.util.Log
import woowa.shopping.di.libs.android.viewModel
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.container.startDI
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startDI {
            container {
                single<ProductRepository> { ProductRepositoryImpl() }
                single<CartRepository> { CartRepositoryImpl() }
                viewModel<MainViewModel>()
                viewModel<CartViewModel>()
            }
        }
    }
}