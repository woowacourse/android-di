package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowacourse.di.Inject
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.toProduct

class CartViewModel
    @Inject
    constructor(
        private val cartRepository: CartRepository,
    ) : ViewModel() {
        private val _cartProducts: MutableLiveData<List<Product>> =
            MutableLiveData(emptyList())
        val cartProducts: LiveData<List<Product>> get() = _cartProducts

        private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
        val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

        fun getAllCartProducts() {
            viewModelScope.launch {
                _cartProducts.value = cartRepository.getAllCartProducts().map { it.toProduct() }
            }
        }

        fun deleteCartProduct(id: Int) {
            viewModelScope.launch {
                cartRepository.deleteCartProduct(id.toLong())
                _onCartProductDeleted.value = true
            }
        }
    }
