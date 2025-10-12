package woowacourse.shopping.di

import android.content.Context

object ApplicationContextProvider {
    private const val ERROR_CONTEXT_IS_NULL: String = "현재 context 는 null 입니다."
    private var appContext: Context? = null
    val applicationContext: Context
        get() =
            appContext ?: run {
                throw IllegalArgumentException(ERROR_CONTEXT_IS_NULL)
            }

    fun setupApplicationContext(context: Context) {
        this.appContext = context.applicationContext
    }
}
