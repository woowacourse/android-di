package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.alsonglibrary2.di.LifecycleViewModel
import com.example.alsonglibrary2.di.anotations.FieldInject
import com.example.alsonglibrary2.di.anotations.ViewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.util.QualifiedCartRepository

class MainViewModel : LifecycleViewModel() {
    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded

    @FieldInject
    @ViewModelScope
    private lateinit var productRepository: ProductRepository

    @FieldInject
    @QualifiedCartRepository
    private lateinit var cartRepository: CartRepository

    fun addCartProduct(product: Product) {
        viewModelScope.launch {
            cartRepository.addCartProduct(product)
            _onProductAdded.value = true
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            _products.value = productRepository.getAllProducts()
        }
    }
}
