package woowacourse.shopping.module

import olive.di.DIModule
import olive.di.annotation.Qualifier
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

@Qualifier
annotation class InMemoryCart

@Qualifier
annotation class RoomCart

abstract class RepositoryDIModule : DIModule {
    @InMemoryCart
    abstract fun bindInMemoryCartRepository(inMemoryCartRepository: InMemoryCartRepository): CartRepository

    @RoomCart
    abstract fun bindRoomCartRepository(roomCartRepository: RoomCartRepository): CartRepository

    abstract fun bindProductRepository(defaultProductRepository: DefaultProductRepository): ProductRepository
}
