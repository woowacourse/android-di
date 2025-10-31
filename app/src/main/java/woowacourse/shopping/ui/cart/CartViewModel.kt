package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import woowacourse.shopping.di.RoomDBCartRepository
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import javax.inject.Inject

@HiltViewModel
class CartViewModel
    @Inject
    constructor(
        @RoomDBCartRepository private val cartRepository: CartRepository,
    ) : ViewModel() {
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

        fun deleteCartProduct(id: Int) {
            viewModelScope.launch {
                cartRepository.deleteCartProduct(id)
            }
            _onCartProductDeleted.value = true
        }
    }
