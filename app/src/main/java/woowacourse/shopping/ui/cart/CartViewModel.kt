package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.repository.CartRepository

class CartViewModel(
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _cartProducts: MutableLiveData<List<CartProduct>> = MutableLiveData(emptyList())
    val cartProducts: LiveData<List<CartProduct>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun fetchAllCartProducts() {
        _cartProducts.value = cartRepository.getAllCartProducts()
    }

    fun deleteCartProduct(id: Int) {
        cartRepository.deleteCartProduct(id)
        _onCartProductDeleted.value = true
    }
}
