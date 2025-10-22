package woowacourse.shopping.di

import com.yrsel.di.DependencyContainer
import com.yrsel.di.Module
import com.yrsel.di.annotation.Provides
import woowacourse.shopping.ui.cart.DateFormatter

class UtilModule : Module {
    @Provides
    fun provideDateFormatter(): DateFormatter = DateFormatter(DependencyContainer.activityContext())
}
