package woowacourse.shopping.ui.products

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
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.di.CustomFieldInjection
import woowacourse.shopping.data.di.InMemory
import woowacourse.shopping.data.di.RoomBacked
import woowacourse.shopping.model.Product

data class ProductsUiState(
    val products: List<Product> = emptyList(),
)

class ProductsViewModel : ViewModel() {

    @field:CustomFieldInjection
    @field:InMemory
    lateinit var productRepository: ProductRepository

    @field:CustomFieldInjection
    @field:RoomBacked
    lateinit var cartRepository: CartRepository

    private val _uiState: MutableStateFlow<ProductsUiState> = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> get() = _uiState.asStateFlow()

    private val _onProductAdded: MutableSharedFlow<Unit> = MutableSharedFlow()
    val onProductAdded: SharedFlow<Unit> get() = _onProductAdded.asSharedFlow()

    fun getAllProducts() {
        _uiState.update { it.copy(products = productRepository.getAllProducts()) }
    }

    fun addCartProduct(product: Product) {
        viewModelScope.launch {
            cartRepository.addCartProduct(product)
            _onProductAdded.emit(Unit)
        }
    }
}
