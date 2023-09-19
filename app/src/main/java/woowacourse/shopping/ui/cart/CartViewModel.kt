package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.angrypig.autodi.lifeCycleScopeHandler.AutoDiScopedViewModel
import com.angrypig.autodi.lifeCycleScopeHandler.Scoped
import kotlinx.coroutines.launch
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.repository.CartRepository

class CartViewModel @Scoped constructor(
    private val cartRepository: CartRepository,
) : AutoDiScopedViewModel<CartViewModel>() {

    override val registerScope: CartViewModel = this

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

    fun deleteCartProduct(id: Int) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _onCartProductDeleted.value = true
        }
    }
}
