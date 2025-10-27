package woowacourse.shopping.ui.cart

import android.content.Context
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Module
@InstallIn(ActivityComponent::class)
class DateFormatter(
    context: Context,
) {
    private val formatter =
        SimpleDateFormat(
            context.getString(R.string.date_format),
            Locale.KOREA,
        )

    fun formatDate(timestamp: Long): String = formatter.format(Date(timestamp))
}
