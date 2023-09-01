package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

val ViewModelFactory = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
    ): T = with(modelClass) {
        val productRepository = ShoppingApplication.repositoryContainer.productRepository
        val cartRepository = ShoppingApplication.repositoryContainer.cartRepository

        when {
            isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(productRepository, cartRepository)

            isAssignableFrom(CartViewModel::class.java) ->
                CartViewModel(cartRepository)

            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}
