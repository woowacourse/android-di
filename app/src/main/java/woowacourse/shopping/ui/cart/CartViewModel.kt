package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.di.annotation.Database
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

class CartViewModel(
    @Database private val cartRepository: CartRepository,
) : ViewModel() {
    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    suspend fun getAllCartProducts() {
        _cartProducts.value = cartRepository.getAllCartProducts()
    }

    suspend fun deleteCartProduct(id: Long) {
        cartRepository.deleteCartProduct(id)
        _onCartProductDeleted.value = true
    }
}
