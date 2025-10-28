package woowacourse.shopping.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import woowacourse.shopping.ui.cart.DateFormatter

@Module
@InstallIn(ActivityComponent::class)
object AppModule {
    @Provides
    @ActivityScoped
    fun provideDateFormatter(
        @ApplicationContext context: Context,
    ): DateFormatter = DateFormatter(context)
}
