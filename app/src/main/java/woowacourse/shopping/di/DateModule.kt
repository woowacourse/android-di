package woowacourse.shopping.di

import android.content.Context
import com.kmlibs.supplin.annotations.ActivityContext
import com.kmlibs.supplin.annotations.Concrete
import com.kmlibs.supplin.annotations.Module
import woowacourse.shopping.ui.cart.DateFormatter

@Module
object DateModule {
    @Concrete
    fun provideDateFormatter(
        @ActivityContext context: Context,
    ): DateFormatter = DateFormatter(context)
}
