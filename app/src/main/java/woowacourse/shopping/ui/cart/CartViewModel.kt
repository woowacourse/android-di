package woowacourse.shopping.ui.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woosuk.scott_di.Qualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.di.DatabaseCartRepo
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.repository.CartRepository

class CartViewModel (
    @DatabaseCartRepo
    private val cartRepository: CartRepository,
) : ViewModel() {

    init {
        Log.d("wooseok", cartRepository::class.simpleName.toString())
    }
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
