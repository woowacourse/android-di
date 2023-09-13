package woowacourse.shopping.di.constructordi

import com.di.berdi.DIActivity

class FakeConstructorDIActivity : DIActivity() {
    lateinit var viewModel: FakeConstructorDIViewModel
}
