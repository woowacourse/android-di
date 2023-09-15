package woowacourse.shopping.di

import android.content.Context
import com.re4rk.arkdi.ArkContainer
import com.re4rk.arkdi.Singleton
import com.re4rk.arkdi.annotations.ContextType
import com.re4rk.arkdi.annotations.ContextType.Type.ACTIVITY
import woowacourse.shopping.ui.cart.DateFormatter

@Suppress("unused")
class RetainedActivityModule(
    parentDiModule: ArkContainer,
    private val context: Context,
) : ArkContainer(parentDiModule) {
    @Singleton
    @ContextType(ACTIVITY)
    fun provideContext(): Context = context

    @Singleton
    fun provideDateFormatter(
        @ContextType(ACTIVITY) context: Context,
    ): DateFormatter = DateFormatter(context)
}
