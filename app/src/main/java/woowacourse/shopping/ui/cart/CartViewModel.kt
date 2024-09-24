package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowacourse.di.DiViewModel
import com.woowacourse.di.Module
import com.woowacourse.di.annotation.Inject
import com.woowacourse.di.annotation.Qualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.di.ViewModelModule
import woowacourse.shopping.data.mapper.toProducts
import woowacourse.shopping.model.Product

class CartViewModel :
    DiViewModel() {
    override val module: Module = ViewModelModule()

    @Inject
    @Qualifier("InMemory")
    private lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() =
        viewModelScope.launch {
            _cartProducts.value = cartRepository.cartProducts().toProducts()
        }

    fun deleteCartProduct(id: Long) =
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _onCartProductDeleted.value = true
        }
}
