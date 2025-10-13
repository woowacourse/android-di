package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.domain.ProductRepository

class InjectViewModel : ViewModel() {
    @Inject
    lateinit var productRepository: ProductRepository

    fun getProductName(): String = productRepository.getAllProducts().first().name
}
