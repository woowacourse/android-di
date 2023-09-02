package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.ui.cart.CartViewModel

val ShoppingViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

    private val repositoryContainer = ShoppingApplication.repositoryContainer

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                productRepository = repositoryContainer.productRepository,
                cartRepository = repositoryContainer.cartRepository,
            )

            modelClass.isAssignableFrom(CartViewModel::class.java) -> CartViewModel(
                cartRepository = repositoryContainer.cartRepository,
            )

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}
