package woowacourse.shopping.ui.di

import android.content.Context
import com.woowacourse.di.ApplicationContext
import com.woowacourse.di.Module
import woowacourse.shopping.ui.cart.DateFormatter

@Module
class DateFormatterModule {
    fun provideDateFormatter(
        @ApplicationContext context: Context,
    ): DateFormatter = DateFormatter(context)
}
