package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.otterdi.annotation.Inject
import woowacourse.shopping.otterdi.annotation.Qualifier
import woowacourse.shopping.repository.CartRepository

class CartViewModel @Inject constructor(
    @Qualifier("DefaultCartRepository")
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _cartProducts: MutableLiveData<List<CartProduct>> = MutableLiveData(emptyList())
    val cartProducts: LiveData<List<CartProduct>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun fetchAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = cartRepository.getAllCartProducts()
        }
    }

    fun deleteCartProduct(cartProduct: CartProduct) {
//        val deleteItemId = _cartProducts.value!![position].id
        viewModelScope.launch {
            cartRepository.deleteCartProduct(cartProduct.id)
            _onCartProductDeleted.value = true
        }
    }
}
