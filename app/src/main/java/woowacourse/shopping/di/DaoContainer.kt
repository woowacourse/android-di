package woowacourse.shopping.di

import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao

object DaoContainer : Container {

    val cartProductDao: CartProductDao = ShoppingApplication.database.cartProductDao()
}
