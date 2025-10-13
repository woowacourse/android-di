package woowacourse.shopping.fake

import android.app.Application
import com.m6z1.moongdi.annotation.InMemory
import com.m6z1.moongdi.annotation.RoomDb
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.AppDependencies

class FakeApplication :
    Application(),
    AppDependencies {
    @RoomDb
    override val roomCartRepository: CartRepository = FakeRoomCartRepository()

    @InMemory
    override val productRepository: ProductRepository = FakeProductRepository()
    override val cartDao = FakeCartProductDao()

    @InMemory
    override val inMemoryCartRepository: CartRepository = FakeInMemoryCartRepository()
}
