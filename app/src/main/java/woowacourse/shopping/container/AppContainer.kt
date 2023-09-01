package woowacourse.shopping.container

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class AppContainer {
    private val cartRepository: CartRepository = CartRepositoryImpl()
    private val productRepository: ProductRepository = ProductRepositoryImpl()

    val mainViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            MainViewModel(productRepository, cartRepository)
        }
    }

    val cartViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            CartViewModel(cartRepository)
        }
    }
}
