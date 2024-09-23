package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.library.haeum.di.HaeumInject
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.di.RoomDBRepository
import woowacourse.shopping.model.Product

class CartViewModel : ViewModel() {
    @HaeumInject
    @RoomDBRepository
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

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _cartProducts.value =
                _cartProducts.value?.filter { it.id != id } ?: emptyList()
            _onCartProductDeleted.value = true
        }
    }
}
