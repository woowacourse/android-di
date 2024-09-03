package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.DIContainer
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel

class DIViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainViewModel::class.java -> {
                val productRepository = DIContainer.resolve(ProductRepository::class.java)
                val cartRepository = DIContainer.resolve(CartRepository::class.java)
                MainViewModel(productRepository, cartRepository) as T
            }

            CartViewModel::class.java -> {
                val cartRepository = DIContainer.resolve(CartRepository::class.java)
                CartViewModel(cartRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
