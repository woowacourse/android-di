package woowacourse.shopping.fixture

import androidx.lifecycle.ViewModel
import com.example.di2.Inject

class FieldInjectViewModel : ViewModel() {
    @Inject
    lateinit var injectedRepository: FakeProductRepository

    var notInjectedRepository: FakeProductRepository? = null
}
