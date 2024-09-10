package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.annotation.FieldInject
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.ProductRepository

class MainViewModel : ViewModel() {
    @FieldInject
    private lateinit var productRepository: ProductRepository
    @FieldInject
    private lateinit var cartRepository: CartRepository

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
