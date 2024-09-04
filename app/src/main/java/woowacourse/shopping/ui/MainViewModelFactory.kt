package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ShoppingApplication

class MainViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
    ): T {
        return MainViewModel(
            productRepository = ShoppingApplication.productRepository,
            cartRepository = ShoppingApplication.cartRepository,
        ) as T
    }
}