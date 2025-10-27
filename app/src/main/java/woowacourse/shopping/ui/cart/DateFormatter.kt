package woowacourse.shopping.ui.cart

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@ActivityRetainedScoped
class DateFormatter
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        private val formatter =
            SimpleDateFormat(
                context.getString(R.string.date_format),
                Locale.KOREA,
            )

        fun formatDate(timestamp: Long): String = formatter.format(Date(timestamp))
    }
