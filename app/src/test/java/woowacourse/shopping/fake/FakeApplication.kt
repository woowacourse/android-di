package woowacourse.shopping.fake

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.di.AppDependencies

class FakeApplication :
    Application(),
    AppDependencies {
    override val roomCartRepository = FakeRoomCartRepository()
    override val productRepository = FakeProductRepository()
    override val cartDao = FakeCartProductDao()
    override val inMemoryCartRepository: CartRepository = FakeInMemoryCartRepository()
}
