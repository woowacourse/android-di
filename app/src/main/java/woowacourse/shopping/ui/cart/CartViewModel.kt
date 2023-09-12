package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import woowacourse.shopping.common.BandalInject
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartViewModel : ViewModel() {
    @BandalInject
    private lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        _cartProducts.value = cartRepository.getAllCartProducts()
    }

    fun deleteCartProduct(id: Int) {
        cartRepository.deleteCartProduct(id)
        _onCartProductDeleted.value = true
    }
}
