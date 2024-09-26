package woowacourse.shopping.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import woowacourse.shopping.application.di.annotaion.Database
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class ProductViewModel
    @Inject
    constructor(
        private val productRepository: ProductRepository,
        @Database private val cartRepository: CartRepository,
    ) : ViewModel() {
        private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
        val products: LiveData<List<Product>> get() = _products

        private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
        val onProductAdded: LiveData<Boolean> get() = _onProductAdded

        fun addCartProduct(product: Product) {
            viewModelScope.launch {
                cartRepository.addCartProduct(product)
                _onProductAdded.value = true
            }
        }

        fun getAllProducts() {
            _products.value = productRepository.getAllProducts()
        }
    }
