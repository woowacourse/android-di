package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.woowacourse.di.DiViewModel
import com.woowacourse.di.Module
import com.woowacourse.di.annotation.Inject
import com.woowacourse.di.annotation.Qualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.di.ViewModelModule
import woowacourse.shopping.model.Product

class MainViewModel : DiViewModel() {
    override val module: Module = ViewModelModule()

    @Inject
    private lateinit var productRepository: ProductRepository

    @Inject
    @Qualifier("InMemory")
    private lateinit var cartRepository: CartRepository

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
        _products.value = productRepository.products()
    }
}
