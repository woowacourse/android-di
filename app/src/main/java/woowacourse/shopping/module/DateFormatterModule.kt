package woowacourse.shopping.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.KoreanLocaleDateFormatter

@Module
@InstallIn(ActivityComponent::class)
abstract class DateFormatterModule {
    @Binds
    abstract fun dateFormatter(
        dateFormatter: KoreanLocaleDateFormatter
    ): DateFormatter
}