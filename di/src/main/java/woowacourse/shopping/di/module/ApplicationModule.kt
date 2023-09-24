package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.di.application.DIApplication

open class ApplicationModule(
    val applicationContext: Context,
) : Module() {
    fun inject(application: DIApplication) {
        injectAnnotationFields(application)
    }
}
