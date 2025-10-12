package woowacourse.peto.di.fake

import androidx.lifecycle.ViewModel
import woowacourse.peto.di.annotation.Inject
import woowacourse.peto.di.annotation.Qualifier
import woowacourse.peto.di.fake.repository.cart.CartRepository

class FakeViewModel(
    @Inject private val injectedString: String,
) : ViewModel() {
    lateinit var nonInjectedString: String

    @Inject
    @Qualifier("myCart")
    lateinit var myCartRepository: CartRepository

    @Inject
    @Qualifier("othersCart")
    lateinit var othersCartRepository: CartRepository
}
