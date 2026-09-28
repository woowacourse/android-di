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
import woowacourse.di.FieldInject
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.di.RoomCart
import woowacourse.shopping.model.CartProduct

data class CartUiState(
    val cartProducts: List<CartProduct> = emptyList(),
)

class CartViewModel : ViewModel() {
    // 생성자에서 받지 않는 의존성이므로 AoDi.inject()가 끝나기 전에는 사용하면 안 된다.
    // 외부 코드의 직접 접근은 막되, 리플렉션으로 값을 채울 수 있도록 var로 둔다.
    @FieldInject
    @RoomCart
    private lateinit var cartRepository: CartRepository
    private val _uiState: MutableStateFlow<CartUiState> = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> get() = _uiState.asStateFlow()

    private val _onCartProductDeleted: MutableSharedFlow<Unit> = MutableSharedFlow()
    val onCartProductDeleted: SharedFlow<Unit> get() = _onCartProductDeleted.asSharedFlow()

    fun getAllCartProducts() {
        viewModelScope.launch {
            refreshCartProducts()
        }
    }

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            // 삭제 완료 후 목록을 다시 읽고 성공 신호를 보내야 화면과 알림이 실제 DB 상태를 따른다.
            cartRepository.deleteCartProduct(id)
            refreshCartProducts()
            _onCartProductDeleted.emit(Unit)
        }
    }

    private suspend fun refreshCartProducts() {
        val cartProducts = cartRepository.getAllCartProducts()
        _uiState.update {
            it.copy(cartProducts = cartProducts)
        }
    }
}
