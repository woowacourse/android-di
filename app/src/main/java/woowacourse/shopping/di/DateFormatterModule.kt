package woowacourse.shopping.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import woowacourse.shopping.ui.cart.DateFormatter

@Module
@InstallIn(ActivityComponent::class)
object DateFormatterModule {
    @ActivityScoped
    @Provides
    fun provideDateFormatter(
        @ActivityContext context: Context,
    ) = DateFormatter(context)
}
