package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.di.activity.DIActivity
import woowacourse.shopping.di.container.InstanceContainer

open class ActivityModule(
    val activityContext: Context,
    applicationModule: ApplicationModule,
    instanceContainer: InstanceContainer,
) : Module(applicationModule, instanceContainer) {
    fun inject(diActivity: DIActivity) {
        injectAnnotationFields(diActivity)
    }
}
