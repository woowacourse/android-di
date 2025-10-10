package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.di.InjectField
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.model.Product

class CartViewModel : ViewModel() {
    @InjectField
    private lateinit var cartRepository: CartRepository
    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value =
                cartRepository.getAllCartProducts().map {
                    Product(it.id.toInt(), it.name, it.price, it.imageUrl, it.createdAt)
                }
        }
    }

    fun deleteCartProduct(id: Int) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _cartProducts.value = _cartProducts.value?.filter { it.id != id }
            _onCartProductDeleted.value = true
        }
    }
}
