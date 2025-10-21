package woowacourse.shopping.di

import com.daedan.di.DiApplication
import com.daedan.di.module
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.DateFormatter

fun DiApplication.dateFormatterModule() =
    module {
        scope<CartActivity> {
            scoped { scope -> DateFormatter(get(scope = scope)) }
        }
    }
