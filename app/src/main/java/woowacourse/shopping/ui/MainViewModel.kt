package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.re4rk.arkdi.ArkInject
import com.re4rk.arkdi.Qualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class MainViewModel @ArkInject constructor(
    private val productRepository: ProductRepository,
    @Qualifier("CartInDiskRepository")
    private val cartRepository: CartRepository,
) : ViewModel() {

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
