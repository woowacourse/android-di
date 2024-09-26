package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import shopping.di.DIContainer
import shopping.di.Inject
import shopping.di.Scope
import shopping.di.ScopeAnnotation
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.fake.ICartRepository
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.base.BaseViewModel

class MainViewModel : BaseViewModel() {

    @Inject
    @ScopeAnnotation(Scope.VIEWMODEL)
    lateinit var productRepository: ProductRepository

    @Inject
    @ScopeAnnotation(Scope.APP)
    lateinit var cartRepository: ICartRepository

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded

    init {
        DIContainer.register(
            clazz = ProductRepository::class.java,
            instance = ProductRepository(),
            scope = Scope.VIEWMODEL,
            owner = this
        )

        DIContainer.injectFields(this)
    }

    fun addCartProduct(product: Product) {
        viewModelScope.launch {
            cartRepository.addCartProduct(product.toEntity())
            _onProductAdded.value = true
        }
    }

    fun getAllProducts() {
        _products.value = productRepository.getAllProducts()
    }
}
