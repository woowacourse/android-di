package woowacourse.shopping.di

import android.content.Context
import com.woowa.di.ActivityContext
import com.woowa.di.activity.ActivityComponent
import com.woowa.di.component.InstallIn
import woowacourse.shopping.ui.cart.DateFormatter

@InstallIn(ActivityComponent::class)
object DateFormatBinder {
    fun provideDateFormatter(
        @ActivityContext context: Context,
    ): DateFormatter = DateFormatter(context)
}
