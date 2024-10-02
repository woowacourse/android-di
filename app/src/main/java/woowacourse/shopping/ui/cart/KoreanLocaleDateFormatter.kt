package woowacourse.shopping.ui.cart

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class KoreanLocaleDateFormatter @Inject constructor(
    @ActivityContext context: Context
) : DateFormatter {

    private val formatter = SimpleDateFormat(
        context.getString(R.string.date_format), Locale.KOREA
    )

    override fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}

interface DateFormatter {
    fun formatDate(timestamp: Long): String
}
