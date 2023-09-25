package woowacourse.shopping.data.di

import android.content.Context
import com.hyegyeong.di.DiModule
import com.hyegyeong.di.annotations.Inject
import woowacourse.shopping.ui.cart.DateFormatter

class CartActivityDiModule(private val context: Context) : DiModule {

    fun provideContext(): Context = context
    fun provideDateFormatter(@Inject context: Context): DateFormatter = DateFormatter(context)
}
