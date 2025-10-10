package woowacourse.shopping.ui.cart.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.model.Product

class CartViewModel : ViewModel() {
    @Inject
    @Qualifier("default")
    lateinit var cartRepository: CartRepository

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

    fun deleteCartProduct(index: Int) {
        val deleteProductId = _cartProducts.value?.get(index)?.id ?: return
        viewModelScope.launch {
            cartRepository.deleteCartProduct(deleteProductId)
            _onCartProductDeleted.value = true
        }
    }
}
