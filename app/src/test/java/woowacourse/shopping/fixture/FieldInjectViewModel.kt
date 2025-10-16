package woowacourse.shopping.fixture

import androidx.lifecycle.ViewModel

class FieldInjectViewModel : ViewModel() {
    @Inject
    lateinit var injectedRepository: FakeProductRepository

    var notInjectedRepository: FakeProductRepository? = null
}
