package woowacourse.fake

import woowacourse.shopping.di.annotation.Inject

class FakeViewModel {
    @Inject
    lateinit var injectedString: String

    lateinit var nonInjectedString: String
}
