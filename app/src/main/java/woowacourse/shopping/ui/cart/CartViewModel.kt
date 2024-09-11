package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.model.CartRepository

class CartViewModel : ViewModel() {
    @Inject
    private lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<CartProductEntity>> = MutableLiveData(emptyList())
    val cartProducts: LiveData<List<CartProductEntity>> get() = _cartProducts

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
            _onCartProductDeleted.value = true
        }
    }
}
