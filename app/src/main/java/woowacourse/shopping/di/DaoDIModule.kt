package woowacourse.shopping.di

import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao

class DaoDIModule : DIModule {
    fun bindCartProductDao(shoppingApplication: ShoppingApplication): CartProductDao {
        return shoppingApplication.shoppingDatabase.cartProductDao()
    }
}
