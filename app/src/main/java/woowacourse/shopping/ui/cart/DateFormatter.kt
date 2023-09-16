package woowacourse.shopping.ui.cart

import android.content.Context
import com.mission.androiddi.scope.ActivityScope
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@ActivityScope
class DateFormatter(context: Context) {

    private val formatter = SimpleDateFormat(
        context.getString(R.string.date_format),
        Locale.KOREA,
    )

    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}
