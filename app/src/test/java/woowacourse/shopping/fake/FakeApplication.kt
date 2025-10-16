package woowacourse.shopping.fake

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.InMemory
import woowacourse.shopping.di.RoomDB

class FakeApplication :
    Application(),
    AppDependencies {
    @RoomDB
    override val roomCartRepository: CartRepository = FakeRoomCartRepository()

    @InMemory
    override val productRepository: ProductRepository = FakeProductRepository()

    @InMemory
    override val inMemoryCartRepository: CartRepository = FakeInMemoryCartRepository()
}
