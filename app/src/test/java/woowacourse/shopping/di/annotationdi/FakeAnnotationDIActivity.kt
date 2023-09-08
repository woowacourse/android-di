package woowacourse.shopping.di.annotationdi

import woowacourse.shopping.di.DIActivity

class FakeAnnotationDIActivity : DIActivity() {
    lateinit var viewModel: FakeAnnotationDIViewModel
}
