package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dygames.di.annotation.NotCaching
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.Room
import woowacourse.shopping.model.CartProduct

@NotCaching
class CartViewModel(
    @Room
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _cartProducts: MutableLiveData<List<CartProduct>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<CartProduct>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = cartRepository.getAllCartProducts()
        }
    }

    fun deleteCartProduct(cartProduct: CartProduct) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(cartProduct.identifier)
            _onCartProductDeleted.value = true
        }
    }
}
