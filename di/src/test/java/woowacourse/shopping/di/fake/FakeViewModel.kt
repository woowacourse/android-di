package woowacourse.shopping.di.fake

import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.domain.CartRepository

class FakeViewModel : ViewModel() {
    @Inject
    lateinit var injectedString: String

    lateinit var nonInjectedString: String

    @Inject
    lateinit var fakeCartRepository: CartRepository
}
