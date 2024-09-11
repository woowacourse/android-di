package woowacourse.shopping.module

import com.woowacourse.di.DIModule
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao

class DaoDIModule : DIModule {
    fun bindCartProductDao(shoppingApplication: ShoppingApplication): CartProductDao {
        return shoppingApplication.shoppingDatabase.cartProductDao()
    }
}
