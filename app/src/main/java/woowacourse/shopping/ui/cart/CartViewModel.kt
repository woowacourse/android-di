package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.di.Database
import woowacourse.di.InjectField
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.CartProduct

class CartViewModel : ViewModel() {
    @InjectField
    @Database
    private lateinit var cartRepository: CartRepository

    private val _cartProducts = MutableLiveData<List<CartProduct>>(emptyList())
    val cartProducts: LiveData<List<CartProduct>> get() = _cartProducts

    private val _onCartProductDeleted = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() =
        viewModelScope.launch {
            _cartProducts.value = cartRepository.getAllCartProducts()
        }

    fun deleteCartProduct(id: Long) =
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _onCartProductDeleted.value = true
            _cartProducts.value = cartRepository.getAllCartProducts()
        }
}
