package woowacourse.shopping.ui.cart

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatter(
    pattern: String,
) {
    private val formatter =
        SimpleDateFormat(
            pattern,
            Locale.KOREA,
        )

    fun formatDate(timestamp: Long): String = formatter.format(Date(timestamp))
}
