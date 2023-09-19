package woowacourse.shopping.di.module

import android.content.Context
import com.bignerdranch.android.koala.DiModule
import com.bignerdranch.android.koala.KoalaSingleton
import woowacourse.shopping.ui.cart.DateFormatter

class CartActivityDiModule : DiModule {

    override var context: Context? = null

    @KoalaSingleton
    fun getDateFormatter(): DateFormatter {
        return context?.let {
            DateFormatter(it)
        } ?: throw NullPointerException("context가 초기화되지 않았습니다.")
    }
}
