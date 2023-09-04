package woowacourse.shopping.data

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class ShoppingContainer {

    private val cartRepository: CartRepository by lazy {
        CartRepositoryImpl()
    }

    val cartViewModel: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            CartViewModel(cartRepository)
        }
    }

    private val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl()
    }

    val mainViewModel: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            MainViewModel(productRepository, cartRepository)
        }
    }
}
