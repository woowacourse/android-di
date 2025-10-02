package woowacourse.shopping.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.AppContainer
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class ShoppingViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(appContainer.productRepository, appContainer.cartRepository) as T
            }

            CartViewModel::class.java -> {
                CartViewModel(appContainer.cartRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
}
