package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.DIContainer
import com.example.di.FieldInject
import com.example.di.annotations.Qualifier
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.model.Product

class CartViewModel : ViewModel() {
    @FieldInject
    @Qualifier("room")
    private lateinit var cartRepository: CartRepository

    private val _cartProducts: MutableLiveData<List<Product>> =
        MutableLiveData(emptyList())
    val cartProducts: LiveData<List<Product>> get() = _cartProducts

    private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

    init {
        DIContainer.injectFieldDependencies(this)
    }

    fun getAllCartProducts() {
        viewModelScope.launch {
            val job =
                async {
                    _cartProducts.value = cartRepository.getAllCartProducts()
                }
            job.await()
        }
        println()
    }

    fun deleteCartProduct(id: Int) {
        viewModelScope.launch {
            val job =
                async {
                    cartRepository.deleteCartProduct(id)
                    _onCartProductDeleted.value = true
                }
            job.await()
            println("CartViewModel.deleteCartProduct id : $id")
        }
    }
}
