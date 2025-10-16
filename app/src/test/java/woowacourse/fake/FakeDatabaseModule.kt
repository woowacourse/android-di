package woowacourse.fake

import woowacourse.peto.di.DependencyModule
import woowacourse.shopping.data.db.CartProductDao

class FakeDatabaseModule : DependencyModule {
    val dao: CartProductDao = FakeCartProductDao()
}
