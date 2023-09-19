package woowacourse.shopping.di

import android.content.Context

open class ActivityModule(
    val activityContext: Context,
    applicationModule: Module,
) : Module(applicationModule) {
    fun inject(diActivity: DIActivity) {
        injectAnnotationFields(diActivity)
    }
}
