package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.model.Product

class CartViewModel(
    private val cartRepositoryImpl: CartRepository,
) : ViewModel() {

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        _cartProducts.value = cartRepositoryImpl.getAllCartProducts()
    }

    fun deleteCartProduct(id: Int) {
        cartRepositoryImpl.deleteCartProduct(id)
        _onCartProductDeleted.value = true
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CartViewModel(
                    cartRepositoryImpl = CartRepositoryImpl(),
                )
            }
        }
    }
}
