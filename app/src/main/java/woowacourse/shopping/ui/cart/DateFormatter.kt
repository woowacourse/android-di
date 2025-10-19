package woowacourse.shopping.ui.cart

import android.content.Context
import com.example.di.Inject
import woowacourse.shopping.R
import woowacourse.shopping.di.annotation.ActivityContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatter
    @Inject constructor(
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
