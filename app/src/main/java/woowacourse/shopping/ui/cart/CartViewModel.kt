package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.di.DIScopeManager
import woowacourse.di.ScopeType
import woowacourse.di.annotation.Inject
import woowacourse.di.annotation.RoomDB
import woowacourse.di.annotation.SingletonScope
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.model.CartProduct

class CartViewModel : ViewModel() {
    @Inject
    @RoomDB
    @SingletonScope
    private lateinit var cartRepository: CartRepository

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

    override fun onCleared() {
        super.onCleared()
        DIScopeManager.clearScope(ScopeType.ViewModel)
    }
}
