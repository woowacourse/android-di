package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository

class CartViewModelFactory(
    private val cartRepository: CartRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(cartRepository) as T
        }
        throw IllegalArgumentException(MESSAGE_UNKNOWN_VIEWMODEL_CLASS)
    }

    companion object {
        private const val MESSAGE_UNKNOWN_VIEWMODEL_CLASS = "Unknown ViewModel class"
    }
}
