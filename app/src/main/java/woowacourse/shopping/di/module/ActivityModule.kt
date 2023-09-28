package woowacourse.shopping.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import woowacourse.shopping.ui.cart.DateFormatter

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    fun provideDateFormatter(@ActivityContext context: Context): DateFormatter =
        DateFormatter(context)
}
