package woowacourse.shopping.data.db

import woowacourse.shopping.di.DependencyModule

class DatabaseModule(
    private val database: ShoppingDatabase,
) : DependencyModule, DatabaseProvider {
    override val cartDao: CartProductDao by lazy { database.cartProductDao() }
}
