package woowacourse.shopping.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.on.di_library.di.DiViewModel
import com.on.di_library.di.annotation.MyInjector
import com.on.di_library.di.annotation.MyQualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.ProductRepository

class MainViewModel : DiViewModel() {
    @MyInjector
    private lateinit var productRepository: ProductRepository

    @MyInjector
    @MyQualifier("default")
    private lateinit var cartRepository: CartRepository

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded

    fun addCartProduct(product: Product) {
        viewModelScope.launch {
            runCatching {
                cartRepository.addCartProduct(product)
            }.onSuccess {
                _onProductAdded.value = true
            }.onFailure {
                Log.e("TAG", "addCartProduct: 추가됨 실패", it)
            }
        }
    }

    fun getAllProducts() {
        _products.value = productRepository.getAllProducts()
    }
}
