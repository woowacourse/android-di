package woowacourse.shopping.di.manual

import androidx.lifecycle.ViewModelProvider

class ManualViewModelFactory : ViewModelProvider.Factory {
    /*
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
     */
}
