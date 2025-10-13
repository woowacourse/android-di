package woowacourse.shopping.ui.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ShoppingContainer
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class AutoViewModelFactory(
    private val container: ShoppingContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = modelClass.getDeclaredConstructor().newInstance()

        if (viewModel is CartViewModel) {
            viewModel.cartRepository = container.get(CartRepository::class)
        } else if (viewModel is MainViewModel) {
            viewModel.productRepository = container.get(ProductRepository::class)
            viewModel.cartRepository = container.get(CartRepository::class)
        }

        return viewModel
    }
}
