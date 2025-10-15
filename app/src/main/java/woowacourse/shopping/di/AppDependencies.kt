package woowacourse.shopping.di

import com.m6z1.moongdi.annotation.InMemory
import com.m6z1.moongdi.annotation.RoomDb
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

interface AppDependencies {
    @RoomDb
    val roomCartRepository: CartRepository

    @InMemory
    val productRepository: ProductRepository

    @InMemory
    val inMemoryCartRepository: CartRepository
}
