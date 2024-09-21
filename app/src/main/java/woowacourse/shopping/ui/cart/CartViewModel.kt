package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.Inject
import com.example.di.InjectedViewModel
import com.example.di.Qualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.data.local.LocalCartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.CartRepository

@InjectedViewModel
class CartViewModel : ViewModel() {
    @Inject
    @Qualifier(LocalCartRepository::class)
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
            _cartProducts.value = _cartProducts.value?.filter { it.id != id }
            _onCartProductDeleted.value = true
        }
    }
}
