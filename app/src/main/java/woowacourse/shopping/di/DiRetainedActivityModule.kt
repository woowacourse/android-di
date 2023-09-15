package woowacourse.shopping.di

import android.content.Context
import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdi.Singleton
import com.re4rk.arkdi.annotations.ContextType
import com.re4rk.arkdi.annotations.ContextType.Type.ACTIVITY
import woowacourse.shopping.ui.cart.DateFormatter

@Suppress("unused")
class DiRetainedActivityModule(
    parentDiModule: DiContainer,
    private val context: Context,
) : DiContainer(parentDiModule) {
    @Singleton
    @ContextType(ACTIVITY)
    fun provideContext(): Context = context

    @Singleton
    fun provideDateFormatter(
        @ContextType(ACTIVITY) context: Context,
    ): DateFormatter = DateFormatter(context)
}
