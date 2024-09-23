package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.seogi.di.DiViewModel
import com.example.seogi.di.annotation.FieldInject
import kotlinx.coroutines.launch
import woowacourse.shopping.di.OnDisk
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class MainViewModel(
    private val productRepository: ProductRepository,
) : DiViewModel() {
    @FieldInject
    @OnDisk
    lateinit var cartRepository: CartRepository

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
