package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.model.Product

class CartViewModel(
    private val defaultCartRepository: DefaultCartRepository,
) : ViewModel() {

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        _cartProducts.value = defaultCartRepository.getAllCartProducts()
    }

    fun deleteCartProduct(id: Int) {
        defaultCartRepository.deleteCartProduct(id)
        _onCartProductDeleted.value = true
    }
}
