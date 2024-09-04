package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.model.Product

class MainViewModel(
    private val productDefaultRepository: ProductRepository,
    private val cartDefaultRepository: CartRepository,
) : ViewModel() {

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded


    fun addCartProduct(product: Product) {
        cartDefaultRepository.addCartProduct(product)
        _onProductAdded.value = true
    }

    fun getAllProducts() {
        _products.value = productDefaultRepository.getAllProducts()
    }
}
