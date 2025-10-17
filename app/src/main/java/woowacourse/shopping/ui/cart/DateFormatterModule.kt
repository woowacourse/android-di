package woowacourse.shopping.ui.cart

import android.content.Context
import woowacourse.peto.di.DependencyModule
import woowacourse.peto.di.annotation.Inject

class DateFormatterModule(
    private val context: Context,
) : DependencyModule {
    @Inject
    val dateFormatter: DateFormatter by lazy { DateFormatter(context) }
}
