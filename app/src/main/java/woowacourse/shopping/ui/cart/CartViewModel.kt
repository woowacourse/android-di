package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.di.FieldInject
import woowacourse.shopping.model.Product

class CartViewModel : ViewModel() {
    private val _cartProducts: MutableLiveData<MutableList<Product>> =
        MutableLiveData(mutableListOf())
    val cartProducts: LiveData<MutableList<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    @FieldInject
    private lateinit var cartRepository: CartRepository

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
