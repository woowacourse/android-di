package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartViewModel(
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        _cartProducts.value = cartRepository.getAllCartProducts()
    }

    fun deleteCartProduct(id: Int) {
        cartRepository.deleteCartProduct(id)
        _onCartProductDeleted.value = true
    }

    class CartViewModelFactory(
        private val cartRepository: CartRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                return CartViewModel(cartRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
