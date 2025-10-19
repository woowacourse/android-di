package woowacourse.shopping.di

import com.yrsel.di.ContextProvider
import com.yrsel.di.Module
import com.yrsel.di.annotation.ActivityScope
import com.yrsel.di.annotation.Provides
import woowacourse.shopping.ui.cart.DateFormatter

class UtilModule : Module {
    @Provides
    @ActivityScope
    fun provideDateFormatter(): DateFormatter {
        val context = ContextProvider.getContext()
        return DateFormatter(context)
    }
}
