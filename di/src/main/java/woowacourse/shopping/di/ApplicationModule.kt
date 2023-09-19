package woowacourse.shopping.di

import android.content.Context

open class ApplicationModule(
    val applicationContext: Context,
) : Module {
    fun inject(application: DIApplication) {
        Injector(this).injectAnnotationFields(application::class)
    }
}
