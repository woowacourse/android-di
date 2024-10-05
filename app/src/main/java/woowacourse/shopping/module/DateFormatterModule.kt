package woowacourse.shopping.module

import android.content.Context
import org.koin.dsl.module
import woowacourse.shopping.presentation.cart.CartActivity
import woowacourse.shopping.presentation.cart.DateFormatter
import woowacourse.shopping.presentation.cart.KoreanLocaleDateFormatter

val dateFormatterModule = module {
    scope<CartActivity> {
        scoped<DateFormatter> { (activityContext: Context) -> KoreanLocaleDateFormatter(activityContext) }
    }
}