package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.repository.CartDefaultRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.util.ReflectiveViewModelFactory

class MainViewModel(
    private val productDefaultRepository: ProductRepository,
    private val cartDefaultRepository: CartRepository,
) : ViewModel() {

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded


    fun addCartProduct(product: Product) {
        cartDefaultRepository.addCartProduct(product)
        _onProductAdded.value = true
    }

    fun getAllProducts() {
        _products.value = productDefaultRepository.getAllProducts()
    }

    companion object {
        fun factory(
            productRepository: ProductRepository = ProductDefaultRepository(),
            cartRepository: CartRepository = CartDefaultRepository(),
        ): ViewModelProvider.Factory {
            return MainViewModelFactory(productRepository, cartRepository)
        }

        fun reflectionFactory(): ViewModelProvider.Factory {
            return ReflectiveViewModelFactory()
        }
    }
}
