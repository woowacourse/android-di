package woowacourse.shopping.ui.cart

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Prototype
import woowacourse.shopping.R
import java.time.LocalDateTime
import java.util.Locale

@Prototype
@Component
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
