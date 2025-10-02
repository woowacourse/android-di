package woowacourse.shopping.di.manual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class ManualViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java ->
                MainViewModel(
                    productRepository = ManualInjection.productRepository,
                    cartRepository = ManualInjection.cartRepository,
                ) as T

            CartViewModel::class.java ->
                CartViewModel(
                    cartRepository = ManualInjection.cartRepository,
                ) as T

            else -> error("Unknown ViewModel: ${modelClass.name}")
        }
}
