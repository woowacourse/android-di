package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzang.di.DIContainer
import com.zzang.di.DependencyInjector
import com.zzang.di.annotation.Inject
import com.zzang.di.annotation.QualifierType
import kotlinx.coroutines.launch
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct

class CartViewModel : ViewModel() {
    @Inject(qualifier = QualifierType.DATABASE)
    lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<CartProduct>> = MutableLiveData(emptyList())
    val cartProducts: LiveData<List<CartProduct>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    init {
        DependencyInjector.injectDependencies(this, this)
    }

    fun getAllCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = cartRepository.getAllCartProducts()
        }
    }

    fun deleteCartProduct(id: Long) {
        viewModelScope.launch {
            try {
                cartRepository.deleteCartProduct(id)
                _onCartProductDeleted.value = true
                getAllCartProducts()
            } catch (e: Exception) {
                _onCartProductDeleted.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        DIContainer.clearViewModelScopedInstances(this)
    }
}
