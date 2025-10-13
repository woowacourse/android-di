package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopping.di.annotation.Inject
import com.shopping.di.annotation.QualifierTag
import kotlinx.coroutines.launch
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class CartViewModel : ViewModel() {
    @Inject
    @QualifierTag("memory")
    private lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = cartRepository.getAllCartProducts()
        }
    }

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _cartProducts.value = cartProducts.value?.filter { it.id != id }
            _onCartProductDeleted.value = true
        }
    }
}
