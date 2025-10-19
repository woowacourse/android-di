package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.android.di.AndroidContainer
import woowacourse.shopping.android.di.Scope
import woowacourse.shopping.data.PersistentCartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.CartRepository

class CartViewModel : ViewModel() {
    private val cartRepository: CartRepository =
        AndroidContainer.instance(
            CartRepository::class,
            Scope.ApplicationScope,
            PersistentCartRepository.QUALIFIER,
        )

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

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _onCartProductDeleted.value = true
        }
    }
}
