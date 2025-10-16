package woowacourse.shopping.data.db

import woowacourse.peto.di.DependencyModule

class DatabaseModule(
    private val database: ShoppingDatabase,
) : DependencyModule {
    val cartDao: CartProductDao by lazy { database.cartProductDao() }
}
