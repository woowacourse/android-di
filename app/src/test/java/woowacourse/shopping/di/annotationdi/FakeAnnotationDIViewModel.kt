package woowacourse.shopping.di.annotationdi

import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.Inject

class FakeAnnotationDIViewModel : ViewModel() {
    @Inject
    lateinit var fakeObj: FakeObj
}
