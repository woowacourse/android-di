package woowacourse.shopping.di

import android.content.Context

open class ApplicationModule(
    val applicationContext: Context,
) : Module() {
    fun inject(application: DIApplication) {
        injectAnnotationFields(application)
    }
}
