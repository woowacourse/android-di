package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.Inject
import com.example.di.RoomDatabase
import kotlinx.coroutines.launch
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.ui.model.CartUiModel
import woowacourse.shopping.ui.model.toPresentation

class CartViewModel : ViewModel() {
    @Inject
    @RoomDatabase
    lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<CartUiModel>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<CartUiModel>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = cartRepository.getAllCartProducts().map { it.toPresentation() }
        }
    }

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            getAllCartProducts()
            _onCartProductDeleted.value = true
        }
    }
}
