package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.medandro.di.BaseViewModel
import com.medandro.di.annotation.InjectField
import com.medandro.di.annotation.LifecycleScope
import com.medandro.di.annotation.Qualifier
import kotlinx.coroutines.launch
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.model.Product

class CartViewModel : BaseViewModel() {
    @InjectField(scope = LifecycleScope.APPLICATION)
    @Qualifier("RoomDB") // InMemory 로 바꾸면 InMemory 사용 (클린빌드 필요)
    private lateinit var cartRepository: CartRepository
    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value =
                cartRepository.getAllCartProducts()
        }
    }

    fun deleteCartProduct(id: Int) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(id)
            _cartProducts.value = _cartProducts.value?.filter { it.id != id }
            _onCartProductDeleted.value = true
        }
    }
}
