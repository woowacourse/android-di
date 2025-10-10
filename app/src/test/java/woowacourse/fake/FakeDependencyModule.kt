package woowacourse.fake

import woowacourse.shopping.core.DependencyModule

class FakeDependencyModule : DependencyModule {
    val stringProvider: String = "inject"
}
