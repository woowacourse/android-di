package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.Inject
import com.example.di.InjectedViewModel
import com.example.di.Qualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.local.LocalCartRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.ProductRepository

@InjectedViewModel
class MainViewModel : ViewModel() {
    @Inject
    @Qualifier(DefaultProductRepository::class)
    private lateinit var productRepository: ProductRepository

    @Inject
    @Qualifier(LocalCartRepository::class)
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
