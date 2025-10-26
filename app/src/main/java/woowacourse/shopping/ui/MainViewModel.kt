package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.annotation.Inject
import com.example.di.annotation.Qualifier
import com.example.di.annotation.Scope
import com.example.di.annotation.ScopeType
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.model.Product

class MainViewModel : ViewModel() {
    @Inject
    @Qualifier(DefaultProductRepository::class)
    @Scope(ScopeType.SINGLETON)
    private lateinit var productRepository: ProductRepository

    @Inject
    @Qualifier(DefaultCartRepository::class)
    @Scope(ScopeType.SINGLETON)
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
