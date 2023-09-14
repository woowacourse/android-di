package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boogiwoogi.di.WoogiQualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.model.CartRepository

class CartViewModel(
    @WoogiQualifier(InMemoryCartRepository::class)
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
