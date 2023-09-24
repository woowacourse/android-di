package woowacourse.shopping.di

import android.content.Context

open class ActivityModule(
    val activityContext: Context,
    applicationModule: ApplicationModule,
) : Module(applicationModule) {
    fun inject(diActivity: DIActivity) {
        injectAnnotationFields(diActivity)
    }
}
