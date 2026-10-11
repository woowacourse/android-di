package woowacourse.shopping.ui.cart

import androidx.lifecycle.viewModelScope
import com.example.di.Scope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import woowacourse.shopping.ScopedViewModel
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.util.annotations.RoomRepo

data class CartUiState(
    val cartProducts: List<CartProduct> = emptyList(),
)

class CartViewModel(
    @RoomRepo
    private val cartRepository: CartRepository,
) : ScopedViewModel() {
    lateinit var screenScope: Scope
        private set

    override fun attachScope(scope: Scope) {
        super.attachScope(scope)
        screenScope = Scope("screen", scope.owner("app"))
    }

    override fun onCleared() {
        if (this::screenScope.isInitialized) {
            screenScope.close()
        }
        super.onCleared()
    }

    private val _uiState: MutableStateFlow<CartUiState> = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> get() = _uiState.asStateFlow()

    private val _onCartProductDeleted: MutableSharedFlow<Unit> = MutableSharedFlow()
    val onCartProductDeleted: SharedFlow<Unit> get() = _onCartProductDeleted.asSharedFlow()

    fun getAllCartProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(cartProducts = cartRepository.getAllCartProducts()) }
        }
    }

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            getAllCartProducts()
            viewModelScope.launch { _onCartProductDeleted.emit(Unit) }
        }
    }
}
