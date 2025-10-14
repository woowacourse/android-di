package woowacourse.shopping.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.core.di.InjectPersistentCartRepository
import woowacourse.shopping.core.di.InjectedProperty
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.ProductRepository

class MainViewModel : ViewModel() {
    @InjectedProperty
    private lateinit var productRepository: ProductRepository

    @InjectedProperty
    @InjectPersistentCartRepository
    private lateinit var cartRepository: CartRepository

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded

    fun addCartProduct(product: Product) {
        viewModelScope.launch {
            cartRepository.addCartProduct(product)
            _onProductAdded.value = true
        }
    }

    fun getAllProducts() {
        _products.value = productRepository.getAllProducts()
    }
}
