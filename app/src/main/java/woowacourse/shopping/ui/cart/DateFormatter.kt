package woowacourse.shopping.ui.cart

import android.content.Context
import com.example.di_v2.annotation.ActivityScoped
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@ActivityScoped
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
