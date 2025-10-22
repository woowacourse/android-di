package woowacourse.shopping.di

import com.daedan.di.DiComponent
import com.daedan.di.module
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.DateFormatter

fun DiComponent.dateFormatterModule() =
    module {
        scope<CartActivity> {
            scoped { scope -> DateFormatter(get(scope = scope)) }
        }
    }
