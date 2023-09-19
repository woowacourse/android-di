package woowacourse.shopping.di

import android.content.Context

open class ActivityModule(
    val activityContext: Context,
) : Module {
    fun inject(diActivity: DIActivity) {
        Injector(this).injectAnnotationFields(diActivity::class)
    }
}
