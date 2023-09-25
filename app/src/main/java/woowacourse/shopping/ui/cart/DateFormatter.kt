package woowacourse.shopping.ui.cart

import android.content.Context
import android.util.Log
import com.boogiwoogi.di.Qualifier
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatter(
    @Qualifier("ActivityContext")
    context: Context
) {

    init {
        Log.d("dataformatter", "created")
    }

    private val formatter = SimpleDateFormat(
        context.getString(R.string.date_format), Locale.KOREA
    )

    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}
