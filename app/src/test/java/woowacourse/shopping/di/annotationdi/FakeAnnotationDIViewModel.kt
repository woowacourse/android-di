package woowacourse.shopping.di.annotationdi

import androidx.lifecycle.ViewModel
import com.di.berdi.annotation.Inject

class FakeAnnotationDIViewModel : ViewModel() {
    @Inject
    lateinit var fakeObj: FakeObj
}
