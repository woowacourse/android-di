package woowacourse.shopping.ui.cart

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import woowacourse.shopping.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KoreaLocaleDateFormatter(context: Context) :DateFormatter/*,DefaultLifecycleObserver*/ {
    private val formatter =
        SimpleDateFormat(
            context.getString(R.string.date_format),
            Locale.KOREA,
        )

    override fun formatDate(timestamp: Long): String = formatter.format(Date(timestamp))
//
//    override fun onCreate(owner: LifecycleOwner) {
//        super.onCreate(owner)
//        Log.d(TAG, "onCreate. owner: $owner")
//    }
//
//    override fun onDestroy(owner: LifecycleOwner) {
//        super.onDestroy(owner)
//        Log.d(TAG, "onDestroy. owner: $owner")
//        if((owner as ComponentActivity).isChangingConfigurations){
//            owner.lifecycle.removeObserver(this)
//        }
//
//    }
}

interface DateFormatter {
    fun formatDate(timestamp: Long): String
}

private const val TAG = "KoreaLocaleDateFormatte"