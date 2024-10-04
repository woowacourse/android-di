package woowacourse.shopping.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import woowacourse.shopping.presentation.cart.DateFormatter
import woowacourse.shopping.presentation.cart.KoreanLocaleDateFormatter

@Module
@InstallIn(ActivityComponent::class)
abstract class DateFormatterModule {
    @Binds
    @ActivityScoped
    abstract fun dateFormatter(
        dateFormatter: KoreanLocaleDateFormatter
    ): DateFormatter
}