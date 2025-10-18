package woowacourse.shopping.ui.cart

import android.content.Context
import android.util.Log
import com.example.di.ActivityScope
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@ActivityScope
class DateFormatter(context: Context) {
    init {
        Log.d("TAG", "DI 생명주기: DateFormatter 생성")
    }

    private val formatter =
        SimpleDateFormat(
            context.getString(R.string.date_format),
            Locale.KOREA,
        )

    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}
