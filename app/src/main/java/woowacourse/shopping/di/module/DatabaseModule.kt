package woowacourse.shopping.di.module

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DIContainer

class DatabaseModule(private val shoppingDatabase: ShoppingDatabase) : Module {
    override fun register() {
        DIContainer.register(CartProductDao::class) {
            shoppingDatabase.cartProductDao()
        }
    }
}
