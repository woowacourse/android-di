package woowacourse.shopping.fixture

import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.Inject

class FieldInjectViewModel : ViewModel() {
    @Inject
    lateinit var injectedRepository: FakeProductRepository

    var notInjectedRepository: FakeProductRepository? = null
}
