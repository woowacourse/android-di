package woowacourse.shopping.di.module

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.RoomDB

class DatabaseModule(private val shoppingDatabase: ShoppingDatabase) : Module {
    override fun register() {
        DIContainer.register(CartProductDao::class, RoomDB::class) {
            shoppingDatabase.cartProductDao()
        }
    }
}
