package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.DIContainer
import kotlinx.coroutines.launch
import com.example.di.Inject
import com.example.di.ViewModelScope
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.model.CartProduct

class CartViewModel: ViewModel() {

    @Inject
    private lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<CartProduct>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<CartProduct>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = cartRepository.getAllCartProducts().map { it.toCartProduct() }
        }
    }

    fun deleteCartProduct(id: Int) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id.toLong())
            _onCartProductDeleted.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        DIContainer.clearScope(ViewModelScope::class)
    }
}
