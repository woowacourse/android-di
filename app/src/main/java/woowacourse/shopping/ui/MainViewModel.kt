package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ki960213.sheath.annotation.Inject
import com.ki960213.sheath.annotation.NewInstance
import com.ki960213.sheath.annotation.Qualifier
import com.ki960213.sheath.annotation.SheathViewModel
import kotlinx.coroutines.launch
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

@SheathViewModel
class MainViewModel(
    @Qualifier(InMemoryCartRepository::class)
    private val cartRepository: CartRepository,
) : ViewModel() {

    @NewInstance
    @Inject
    private lateinit var productRepository: ProductRepository

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
