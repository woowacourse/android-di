package woowacourse.shopping.presentation.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.model.Product

class CartViewModel(
    private val cartRepository: CartRepository,
) : ViewModel() {

    init {
        Log.d(TAG, "init: created")
    }

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = cartRepository.getAllCartProducts()
        }
    }

    fun deleteCartProduct(id: Int) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _onCartProductDeleted.value = true
        }
    }
}

private const val TAG = "CartViewModel"