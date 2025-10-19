package woowacourse.shopping.di

import android.content.Context
import com.example.di.ActivityLifespan
import com.example.di.Dependency
import com.example.di.Module
import woowacourse.shopping.ui.cart.DateFormatter

class DateFormatterModule(
    context: Context,
) : Module {
    @Dependency
    @ActivityLifespan
    val dateFormatter: DateFormatter by lazy { DateFormatter(context) }
}
