package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boogiwoogi.di.Qualifier
import com.boogiwoogi.di.Singleton
import kotlinx.coroutines.launch
import woowacourse.shopping.model.CartRepository

class CartViewModel(
    /**
     * todo: 현재는 주입 받는 곳에서 Singleton으로 객체를 만들고 주입받을지 정하고 있다. 이것을 module 딴에서 하면 더 좋을듯 하다.
     */
    @Singleton
    @Qualifier("InMemoryCartRepository")
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _uiState: MutableLiveData<CartUiState> = MutableLiveData(CartUiState())
    val uiState: LiveData<CartUiState>
        get() = _uiState

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            _uiState.value = CartUiState(
                cartProducts = cartRepository.getAllCartProducts(),
                onDelete = ::deleteCartProduct
            )
        }
    }

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _onCartProductDeleted.value = true
        }
    }
}
