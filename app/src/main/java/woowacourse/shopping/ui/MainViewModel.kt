package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.di.annotation.CustomInject
import woowacourse.shopping.di.annotation.DatabaseMode
import woowacourse.shopping.di.annotation.InMemoryMode
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class MainViewModel : ViewModel() {

    @woowacourse.shopping.di.annotation.InMemoryMode
    @woowacourse.shopping.di.annotation.CustomInject
    private lateinit var productRepository: ProductRepository

    @woowacourse.shopping.di.annotation.DatabaseMode
    @woowacourse.shopping.di.annotation.CustomInject
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
