package woowacourse.shopping.ui.cart

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import woowacourse.shopping.R
import java.time.LocalDateTime
import java.util.Locale

class DateFormatter(context: Context) {

    private val formatter = SimpleDateFormat(
        context.getString(R.string.date_format),
        Locale.KOREA,
    )

    fun formatDate(date: LocalDateTime): String {
        val calendar = Calendar.getInstance(Locale.KOREA)
        calendar.set(
            date.year,
            date.monthValue - 1,
            date.dayOfMonth,
            date.hour,
            date.minute,
            date.second,
        )

        return formatter.format(calendar)
    }
}
