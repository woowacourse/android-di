package woowacourse.shopping.ui.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.koala.KoalaFieldInject
import kotlinx.coroutines.launch
import woowacourse.shopping.annotation.RoomDBCartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.repository.CartRepository

class CartViewModel : ViewModel() {

    @KoalaFieldInject
    @RoomDBCartRepository
    lateinit var cartRepository: CartRepository

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
            runCatching {
                cartRepository.deleteCartProduct(id)
            }.onSuccess {
                _onCartProductDeleted.value = true
            }.onFailure {
                _onCartProductDeleted.value = false
                Log.d("ERROR", it.message.toString())
            }
        }
    }
}
