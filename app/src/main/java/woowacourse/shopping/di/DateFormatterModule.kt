package woowacourse.shopping.di

import android.content.Context
import com.example.di.ActivityLifespan
import com.example.di.Dependency
import com.example.di.Module
import woowacourse.shopping.ui.cart.DateFormatter

class DateFormatterModule(
    private val context: Context,
) : Module {
    @Dependency
    @ActivityLifespan
    fun dateFormatter(): DateFormatter = DateFormatter(context)
}
