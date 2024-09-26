package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yennydi.di.Injected
import com.example.yennydi.viewmodel.DiViewModel
import kotlinx.coroutines.launch
import woowacourse.shopping.DataBase
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.CartProduct

class CartViewModel(
    @Injected @DataBase private val cartRepository: CartRepository,
) : DiViewModel() {
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
            _onCartProductDeleted.value = true
        }
    }
}
