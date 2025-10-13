package woowacourse.shopping.fake

import androidx.lifecycle.ViewModel
import woowacourse.bibi.di.core.Inject
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class FakeViewModel : ViewModel() {
    @Inject
    lateinit var productRepository: ProductRepository

    var notInjected: CartRepository? = null
}
