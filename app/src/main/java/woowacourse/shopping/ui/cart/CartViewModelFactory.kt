package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ShoppingApplication

class CartViewModelFactory: ViewModelProvider.Factory  {
    override fun <T: ViewModel> create(
        modelClass: Class<T>,
    ): T {
        return CartViewModel(
            cartRepository = ShoppingApplication.cartRepository
        ) as T
    }
}
