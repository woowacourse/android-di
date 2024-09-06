package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.Inject
import woowacourse.shopping.model.Product

class MainViewModel : ViewModel() {

    @Inject
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var cartRepository: CartRepository

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded

    fun addCartProduct(product: Product) {
        cartRepository.addCartProduct(product)
        _onProductAdded.value = true
    }

    fun getAllProducts() {
        _products.value = productRepository.getAllProducts()
    }
}
