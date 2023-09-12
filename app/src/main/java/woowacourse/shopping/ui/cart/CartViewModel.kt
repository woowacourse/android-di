package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.Qualifier
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartViewModel(
    @Qualifier("PersistentCartRepository")
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _cartProducts: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            runCatching {
                cartRepository.getAllCartProducts()
            }.onSuccess {
                _cartProducts.value = it
            }
        }
    }

    fun deleteCartProduct(position: Int) {
        viewModelScope.launch {
            runCatching {
                val items = cartProducts.value ?: throw NullPointerException("삭제할 아이템이 없습니다.")
                cartRepository.deleteCartProduct(items[position].id)
            }.onSuccess {
                _onCartProductDeleted.value = true
            }
        }
    }
}
