package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import woowacourse.shopping.di.Default
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    @Default
    val cartRepository: CartRepository,
) : ViewModel() {

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            runCatching {
                cartRepository.getAllCartProducts()
            }.onSuccess {
                _cartProducts.value = it
            }.onFailure {}
        }
    }

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            runCatching {
                cartRepository.deleteCartProduct(id)
            }.onSuccess {
                _onCartProductDeleted.value = true
            }.onFailure {}
        }
    }
}
