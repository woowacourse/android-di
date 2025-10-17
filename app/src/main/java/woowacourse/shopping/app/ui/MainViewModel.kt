package woowacourse.shopping.app.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.di.Database
import woowacourse.shopping.di.Inject
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product

class MainViewModel : ViewModel() {
    init {
        Log.d("DI_LIFECYCLE", "MainViewModel 생성됨 (SCOPED: ViewModel)")
    }

    @Inject
    @Database
    lateinit var productRepository: ProductRepository

    @Inject
    @Database
    lateinit var cartRepository: CartRepository

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded

    fun addCartProduct(product: Product) =
        viewModelScope.launch {
            cartRepository.addCartProduct(product)
            _onProductAdded.value = true
        }

    fun getAllProducts() {
        _products.value = productRepository.getAllProducts()
    }
}
