package woowacourse.shopping.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.DatabaseLogger
import com.example.di.InMemoryLogger
import com.example.di.RequireInjection
import com.example.di.scope.AppScope
import com.example.di.scope.ViewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class MainViewModel : ViewModel() {
    @RequireInjection(impl = ProductRepositoryImpl::class, scope = ViewModelScope::class)
    @InMemoryLogger
    private lateinit var productRepository: ProductRepository

    @RequireInjection(impl = CartRepositoryImpl::class, scope = AppScope::class)
    @DatabaseLogger
    private lateinit var cartRepository: CartRepository

    private val _products: MutableLiveData<List<Product>> = MutableLiveData()
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
