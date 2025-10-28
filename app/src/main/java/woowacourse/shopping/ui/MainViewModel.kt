package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import woowacourse.shopping.di.InMemory
import woowacourse.shopping.di.RoomDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.Product
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.domain.toData
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        @RoomDatabase private val cartRepository: CartRepository,
        @InMemory private val productRepository: ProductRepository,
    ) : ViewModel() {
        private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
        val products: LiveData<List<Product>> get() = _products

        private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
        val onProductAdded: LiveData<Boolean> get() = _onProductAdded

        fun addCartProduct(product: Product) {
            viewModelScope.launch {
                cartRepository.addCartProduct(product.toData())
                _onProductAdded.value = true
            }
        }

        fun getAllProducts() {
            _products.value = productRepository.getAllProducts()
        }
    }
