package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.common.CommonViewModelFactory
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

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
        fun getViewModelFactory() = CommonViewModelFactory(CartViewModel::class.java) {
            CartViewModel(cartRepositoryImpl = ShoppingApplication.appContainer.cartRepository)
        }
    }
}
