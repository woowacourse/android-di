package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.ui.cart.CartViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java ->
                MainViewModel(
                    AppContainer.cartRepository,
                    AppContainer.productRepository,
                ) as T

            CartViewModel::class.java -> CartViewModel(AppContainer.cartRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
}
