package woowacourse.shopping.ui.cart

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DateFormatter
    @Inject
    constructor(
        @ActivityContext context: Context,
    ) {
        private val formatter =
            SimpleDateFormat(
                context.getString(R.string.date_format),
                Locale.KOREA,
            )

        fun formatDate(timestamp: Long): String {
            return formatter.format(Date(timestamp))
        }
    }
