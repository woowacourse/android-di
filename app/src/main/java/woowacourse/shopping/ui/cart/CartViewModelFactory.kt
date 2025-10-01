package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl

@Suppress("UNCHECKED_CAST")
class CartViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val cartRepository: CartRepository by lazy { CartRepositoryImpl() }

        return CartViewModel(cartRepository) as T
    }
}
