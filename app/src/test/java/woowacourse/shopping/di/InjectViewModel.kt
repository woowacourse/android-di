package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import woowacourse.shopping.domain.ProductRepository
import woowacouse.shopping.di.annotation.Inject

class InjectViewModel : ViewModel() {
    @Inject
    lateinit var productRepository: ProductRepository

    fun getProductName(): String = productRepository.getAllProducts().first().name
}
