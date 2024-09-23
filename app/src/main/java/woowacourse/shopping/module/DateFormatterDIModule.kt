package woowacourse.shopping.module

import olive.di.DIModule
import olive.di.annotation.ActivityScope
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.ui.cart.DateFormatter

@ActivityScope
class DateFormatterDIModule : DIModule {
    fun bindDateFormatter(shoppingApplication: ShoppingApplication): DateFormatter {
        return DateFormatter(shoppingApplication)
    }
}
