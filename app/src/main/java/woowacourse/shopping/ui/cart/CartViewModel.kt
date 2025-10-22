package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.on.di_library.di.DiViewModel
import com.on.di_library.di.annotation.MyInjector
import com.on.di_library.di.annotation.MyQualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

class CartViewModel : DiViewModel() {
    @MyInjector
    @MyQualifier("default")
    private lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            runCatching {
                cartRepository.getAllCartProducts()
            }.onSuccess {
                _cartProducts.value = it
            }.onFailure {}
        }
    }

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            runCatching {
                cartRepository.deleteCartProduct(id)
            }.onSuccess {
                _onCartProductDeleted.value = true
            }.onFailure {}
        }
    }
}
