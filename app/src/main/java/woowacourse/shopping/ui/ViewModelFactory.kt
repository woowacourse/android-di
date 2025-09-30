package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel

class ViewModelFactory(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java -> MainViewModel(cartRepository, productRepository) as T
            CartViewModel::class.java -> CartViewModel(cartRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
}
