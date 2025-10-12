package woowacourse.shopping.fake

import android.app.Application
import woowacourse.shopping.di.AppDependencies

class FakeApplication :
    Application(),
    AppDependencies {
    override val cartRepository = FakeCartRepository()
    override val productRepository = FakeProductRepository()
    override val cartDao = FakeCartProductDao()
}
