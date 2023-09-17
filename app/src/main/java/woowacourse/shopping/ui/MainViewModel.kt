package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyegyeong.di.annotations.Inject
import kotlinx.coroutines.launch
import woowacourse.shopping.data.di.InMemoryCartRepository
import woowacourse.shopping.data.di.InMemoryProductRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import woowacourse.shopping.model.Product

class MainViewModel
@Inject constructor(
    @InMemoryProductRepository
    private val productRepository: ProductRepository,
    @InMemoryCartRepository
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