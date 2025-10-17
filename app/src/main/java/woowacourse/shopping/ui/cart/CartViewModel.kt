package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.di_v2.ScopedViewModel
import com.example.di_v2.annotation.Database
import com.example.di_v2.annotation.Inject
import kotlinx.coroutines.launch
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

class CartViewModel
    @Inject
    constructor(
        @Database
        private val cartRepository: CartRepository,
    ) : ScopedViewModel() {
        private val _cartProducts: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
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
                _onCartProductDeleted.value = true
            }
        }
    }
