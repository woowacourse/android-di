package woowacourse.shopping.module

import olive.di.DIModule
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao

class DaoDIModule : DIModule {
    fun bindCartProductDao(shoppingApplication: ShoppingApplication): CartProductDao {
        return shoppingApplication.shoppingDatabase.cartProductDao()
    }
}
