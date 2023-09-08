package woowacourse.shopping.di.constructordi

import woowacourse.shopping.di.DIActivity

class FakeConstructorDIActivity : DIActivity() {
    lateinit var viewModel: FakeConstructorDIViewModel
}
