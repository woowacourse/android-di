package woowacourse.shopping.fake

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.InMemory
import woowacourse.shopping.di.RoomDB

class FakeAppContainer {
    @RoomDB
    val roomCartRepository: CartRepository = FakeRoomCartRepository(FakeCartProductDao())

    val productRepository: ProductRepository = FakeProductRepository()

    @InMemory
    val inMemoryCartRepository: CartRepository = FakeInMemoryCartRepository()
}
