package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmlibs.supplin.annotations.SupplinViewModel
import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.annotations.Within
import com.kmlibs.supplin.model.Scope
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.di.DatabaseRepository
import woowacourse.shopping.di.ProductModule
import woowacourse.shopping.model.CartedProduct

class CartViewModel : ViewModel() {
    @Supply
    @DatabaseRepository
    private lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<CartedProduct>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<CartedProduct>> get() = _cartProducts

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
