package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopping.di.annotation.Inject
import com.shopping.di.annotation.QualifierTag
import kotlinx.coroutines.launch
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

class MainViewModel : ViewModel() {
    @Inject
    private lateinit var productRepository: ProductRepository

    @Inject
    @QualifierTag("memory")
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
        viewModelScope.launch {
            _products.value = productRepository.getAllProducts()
        }
    }
}
