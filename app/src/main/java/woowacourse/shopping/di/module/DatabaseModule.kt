package woowacourse.shopping.di.module

import woowacourse.di.DIContainer
import woowacourse.di.annotation.RoomDB
import woowacourse.di.module.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class DatabaseModule(private val shoppingDatabase: ShoppingDatabase) : Module {
    override fun register() {
        DIContainer.register(CartProductDao::class, RoomDB::class) {
            shoppingDatabase.cartProductDao()
        }
    }
}
