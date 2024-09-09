package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository
import woowacourse.shopping.ui.injection.DIInjection

class MainViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel(), DIInjection {
    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded

    init {
        checkConstructor()
    }

    fun addCartProduct(product: Product) {
        cartRepository.addCartProduct(product)
        _onProductAdded.value = true
    }

    fun getAllProducts() {
        _products.value = productRepository.getAllProducts()
    }
}
