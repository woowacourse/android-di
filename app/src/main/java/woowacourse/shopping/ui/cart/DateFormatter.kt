package woowacourse.shopping.ui.cart

import android.content.Context
import com.kmlibs.supplin.annotations.ActivityContext
import com.kmlibs.supplin.annotations.Supply
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatter @Supply constructor(
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
