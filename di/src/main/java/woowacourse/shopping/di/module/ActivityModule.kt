package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.di.activity.DIActivity

open class ActivityModule(
    val activityContext: Context,
    applicationModule: ApplicationModule,
) : Module(applicationModule) {
    fun inject(diActivity: DIActivity) {
        injectAnnotationFields(diActivity)
    }
}
