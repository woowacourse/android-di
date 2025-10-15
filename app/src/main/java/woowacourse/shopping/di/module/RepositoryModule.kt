package woowacourse.shopping.di.module

import woowacourse.di.DIContainer
import woowacourse.di.annotation.RoomDB
import woowacourse.di.module.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class RepositoryModule : Module {
    override fun register() {
        DIContainer.register(ProductRepository::class) { ProductRepositoryImpl() }
        DIContainer.register(CartRepository::class, RoomDB::class) {
            val cartProductDao = DIContainer.get(CartProductDao::class, RoomDB::class)
            CartRepositoryImpl(cartProductDao)
        }
    }
}
