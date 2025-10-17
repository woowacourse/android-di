package woowacourse.shopping.ui.cart

import android.content.Context
import com.example.di.Formatter
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatter(
    context: Context,
) : Formatter {
    private val formatter =
        SimpleDateFormat(
            context.getString(R.string.date_format),
            Locale.KOREA,
        )

    override fun formatDate(timestamp: Long): String = formatter.format(Date(timestamp))
}
