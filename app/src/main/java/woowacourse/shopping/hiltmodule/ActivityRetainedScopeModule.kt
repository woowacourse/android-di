package woowacourse.shopping.hiltmodule

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import woowacourse.shopping.ui.cart.DateFormatter

@Module
@InstallIn(ActivityRetainedComponent::class)
object ActivityRetainedScopeModule {
    @Provides
    @ActivityRetainedScoped
    fun provideDateFormatter(@ApplicationContext context: Context): DateFormatter {
        return DateFormatter(context)
    }
}
