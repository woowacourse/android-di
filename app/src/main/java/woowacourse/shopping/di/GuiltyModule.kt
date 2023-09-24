package woowacourse.shopping.di

import android.content.Context
import com.created.customdi.DiContainer
import com.created.customdi.annotation.ApplicationContext
import woowacourse.shopping.ui.cart.DateFormatter

object GuiltyModule {
    @ApplicationContext
    fun provideApplicationContext(): Context = DiContainer.context as Context

    fun provideDateFormatter(context: Context): DateFormatter = DateFormatter(context)
}
