package woowacourse.shopping.data.di

import android.content.Context
import com.hyegyeong.di.DiModule
import woowacourse.shopping.ui.cart.DateFormatter

class CartActivityDiModule(override var context: Context? = null) : DiModule {

    fun provideDateFormatter(): DateFormatter = DateFormatter(context!!)
}