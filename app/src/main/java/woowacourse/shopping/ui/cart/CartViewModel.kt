package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import javax.inject.Inject

@HiltViewModel
class CartViewModel
    @Inject
    constructor(
        private val cartRepository: CartRepository,
    ) : ViewModel() {
        private val _cartProducts: MutableLiveData<List<CartProduct>> = MutableLiveData(emptyList())
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
                _cartProducts.value = cartProducts.value?.filter { it.id != id }
                _onCartProductDeleted.value = true
            }
        }
    }
