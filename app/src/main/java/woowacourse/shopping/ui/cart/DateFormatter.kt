package woowacourse.shopping.ui.cart

import android.content.Context
import woowacourse.shopping.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateFormatter(context: Context) {

    private val formatter = DateTimeFormatter.ofPattern(
        context.getString(R.string.date_format),
        Locale.KOREA,
    )

    fun formatDate(localDateTime: LocalDateTime): String {
        return localDateTime.format(formatter)
    }
}
