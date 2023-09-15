package woowacourse.shopping.di

import android.content.Context
import com.re4rk.arkdi.DiModule
import com.re4rk.arkdi.Singleton
import com.re4rk.arkdi.annotations.ContextType
import com.re4rk.arkdi.annotations.ContextType.Type.ACTIVITY

@Suppress("unused")
class DiRetainedActivityModule(
    private val context: Context,
) : DiModule() {
    @Singleton
    @ContextType(ACTIVITY)
    fun provideContext(): Context = context
}
