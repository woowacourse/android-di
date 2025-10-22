package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.di.DIContainer
import woowacourse.di.module.Module
import woowacourse.shopping.ui.cart.DateFormatter

class FormatterModule(private val context: Context) : Module {
    override fun register() {
        DIContainer.register(DateFormatter::class) {
            DateFormatter(context = context)
        }
    }
}
