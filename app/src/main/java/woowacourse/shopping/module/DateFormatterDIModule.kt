package woowacourse.shopping.module

import olive.di.DIModule
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.ui.cart.DateFormatter

class DateFormatterDIModule : DIModule {
    fun bindDateFormatter(shoppingApplication: ShoppingApplication): DateFormatter {
        return DateFormatter(shoppingApplication)
    }
}
