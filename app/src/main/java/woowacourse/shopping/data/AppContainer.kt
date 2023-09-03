package woowacourse.shopping.data

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class AppContainer {
    private val productRepository = DefaultProductRepository()
    private val cartRepository = DefaultCartRepository()

    val mainViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            MainViewModel(
                productRepository = productRepository,
                cartRepository = cartRepository
            )
        }
    }

    val cartViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            CartViewModel(cartRepository = cartRepository)
        }
    }
}