package woowacourse.shopping.di

import android.content.Context
import com.kmlibs.supplin.ActivityScopeContainer
import com.kmlibs.supplin.annotations.ActivityContext
import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.annotations.Within
import com.kmlibs.supplin.model.Scope
import woowacourse.shopping.ui.cart.DateFormatter

@Module
@Within(Scope.Activity::class)
interface DateModule {
    fun provideDateFormatter(@ActivityContext context: Context): DateFormatter
}
