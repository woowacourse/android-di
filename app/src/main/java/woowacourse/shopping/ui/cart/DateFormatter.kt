package woowacourse.shopping.ui.cart

import android.content.Context
import woowacourse.shopping.R
import woowacourse.shopping.di.annotation.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatter(@ApplicationContext context: Context) {
    private val formatter =
        SimpleDateFormat(
            context.getString(R.string.date_format),
            Locale.KOREA,
        )

    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}
