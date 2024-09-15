package woowacourse.shopping.ui.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.util.SharedCartRepository

class CartViewModel(
    @SharedCartRepository val cartRepository: CartRepository,
) : ViewModel() {
    init {
        Log.d("alsong", "${cartRepository::class.hashCode()}")
    }
    private val _cartProducts: MutableLiveData<MutableList<Product>> =
        MutableLiveData(mutableListOf())
    val cartProducts: LiveData<MutableList<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = cartRepository.getAllCartProducts().toMutableList()
        }
    }

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            val products = _cartProducts.value ?: emptyList()
            _cartProducts.value?.remove(products.first { it.id == id })
            cartRepository.deleteCartProduct(id)
            _onCartProductDeleted.value = true
        }
    }
}
