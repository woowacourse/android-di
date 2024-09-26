package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import shopping.di.DIContainer
import shopping.di.Inject
import shopping.di.Scope
import shopping.di.ScopeAnnotation
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.mapper.toProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.base.BaseViewModel

class CartViewModel : BaseViewModel() {

    @Inject
    @ScopeAnnotation(Scope.APP)
    lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    init {
        DIContainer.injectFields(this)
    }

    fun getAllCartProducts() {
        viewModelScope.launch {
            val cartEntities = cartRepository.getAllCartProducts()
            _cartProducts.value = cartEntities.map { it.toProduct() }
        }
    }


    fun deleteCartProduct(id: Int) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id.toLong())
            _onCartProductDeleted.value = true
        }
    }
}
