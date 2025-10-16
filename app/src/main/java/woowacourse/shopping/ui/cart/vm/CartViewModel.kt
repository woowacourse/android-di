package woowacourse.shopping.ui.cart.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.Inject
import com.example.di.Qualifier
import com.example.domain.model.CartProduct
import com.example.domain.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    @field:Inject
    @Qualifier("room")
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

    fun deleteCartProduct(id: Int) {
        val cartProduct = _cartProducts.value?.get(id) ?: return
        viewModelScope.launch {
            cartRepository.deleteCartProduct(cartProduct.id)
        }
        _onCartProductDeleted.value = true
    }
}
