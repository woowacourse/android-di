package woowacourse.shopping.hilt.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import woowacourse.shopping.hilt.data.CartRepository
import woowacourse.shopping.hilt.data.ProductRepository
import woowacourse.shopping.hilt.di.qualifier.SingleCartQualifier
import woowacourse.shopping.hilt.model.Product
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    @SingleCartQualifier private val cartRepository: CartRepository,
) : ViewModel() {
    private val _products: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded

    fun addCartProduct(product: Product) {
        viewModelScope.launch {
            cartRepository.addCartProduct(product)
            _onProductAdded.value = true
        }
    }

    fun loadAllProducts() {
        viewModelScope.launch {
            _products.value = productRepository.allProducts()
        }
    }
}
