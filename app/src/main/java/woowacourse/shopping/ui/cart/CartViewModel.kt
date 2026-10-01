package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.di.Inject
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.DeliveryFee
import woowacourse.shopping.model.Product

data class CartUiState(
    val cartProducts: List<CartProduct> = emptyList(),
    val deliveryFee: Int = 0,
)

class CartViewModel(
    private val cartRepository: CartRepository,
) : ViewModel() {
    @Inject
    private lateinit var deliveryFee: DeliveryFee

    val uiState: StateFlow<CartUiState> by lazy {
        cartRepository.getAllCartProducts().map {
            CartUiState(cartProducts = it, deliveryFee = deliveryFee.amount)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState(deliveryFee = deliveryFee.amount)
        )
    }

    private val _onCartProductDeleted: MutableSharedFlow<Unit> = MutableSharedFlow()
    val onCartProductDeleted: SharedFlow<Unit> get() = _onCartProductDeleted.asSharedFlow()


    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _onCartProductDeleted.emit(Unit)
        }
    }
}
