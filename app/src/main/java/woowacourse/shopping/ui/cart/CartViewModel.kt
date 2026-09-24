package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.CartProduct

data class CartUiState(
    val cartProducts: List<CartProduct> = emptyList(),
)

class CartViewModel(
    private val cartRepository: CartRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<CartUiState> = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> get() = _uiState.asStateFlow()

    private val _onCartProductDeleted: MutableSharedFlow<Unit> = MutableSharedFlow()
    val onCartProductDeleted: SharedFlow<Unit> get() = _onCartProductDeleted.asSharedFlow()

    fun getAllCartProducts() {
        viewModelScope.launch { updateCartProducts() }
    }

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            updateCartProducts()

            _onCartProductDeleted.emit(Unit)
        }
    }

    private suspend fun updateCartProducts() {
        val products = cartRepository.getAllCartProducts()

        _uiState.update { it.copy(cartProducts = products) }
    }
}
