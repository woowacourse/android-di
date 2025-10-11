package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.di.RequireInjection
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository

class CartViewModel : ViewModel() {
    @RequireInjection
    lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<Product>> = MutableLiveData()
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    private val cachedCartProducts: MutableList<Product> = mutableListOf()

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            val result = cartRepository.deleteCartProduct(id)
            result
                .onSuccess {
                    val product =
                        cachedCartProducts.find { it.id == id } ?: return@launch
                    cachedCartProducts.remove(product)
                    _onCartProductDeleted.value = true
                    _cartProducts.value = cachedCartProducts.toList()
                }
        }
    }

    fun getAllCartProducts() {
        viewModelScope.launch {
            val result = cartRepository.getAllCartProducts()
            result.onSuccess { cartProducts ->
                _cartProducts.value = cartProducts
                cachedCartProducts.clear()
                cachedCartProducts.addAll(cartProducts)
            }
        }
    }
}
