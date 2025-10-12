package woowacourse.shopping.fake

import androidx.lifecycle.ViewModel
import woowacourse.bibi_di.Inject
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class FakeViewModel : ViewModel() {
    @Inject
    lateinit var productRepository: ProductRepository

    var notInjected: CartRepository? = null
}
