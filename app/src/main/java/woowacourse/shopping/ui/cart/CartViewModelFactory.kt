package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.domain.CartRepository

@Suppress("UNCHECKED_CAST")
class CartViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val cartRepository: CartRepository by lazy { CartRepositoryImpl() }

        return CartViewModel(cartRepository) as T
    }
}
