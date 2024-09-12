package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.Inject
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.ui.CartHandler

class CartViewModel : ViewModel(), CartHandler {
    @Inject
    lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<CartProduct>> = MutableLiveData(emptyList())
    val cartProducts: LiveData<List<CartProduct>> get() = _cartProducts

    private val _deletedCartProductPosition: MutableLiveData<Int?> = MutableLiveData(null)
    val deletedCartProductPosition: LiveData<Int?> get() = _deletedCartProductPosition

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = cartRepository.getAllCartProducts()
        }
    }

    override fun onClickDelete(
        id: Long,
        position: Int,
    ) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _deletedCartProductPosition.value = position
        }
    }
}
