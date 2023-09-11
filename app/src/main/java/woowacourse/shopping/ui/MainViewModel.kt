package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class MainViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded

    fun addCartProduct(product: Product) {
        viewModelScope.launch {
            runCatching {
                cartRepository.addCartProduct(product)
            }.onSuccess {
                _onProductAdded.value = true
            }.onFailure {
                _onProductAdded.value = false
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            runCatching {
                productRepository.getAllProducts()
            }.onSuccess { products ->
                _products.value = products
            }
        }
    }
}
