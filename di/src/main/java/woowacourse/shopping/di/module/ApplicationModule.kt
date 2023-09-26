package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.di.application.DIApplication
import woowacourse.shopping.di.container.InstanceContainer

open class ApplicationModule(
    val applicationContext: Context,
    instanceContainer: InstanceContainer,
) : Module(container = instanceContainer) {
    fun inject(application: DIApplication) {
        injectAnnotationFields(application)
    }
}
