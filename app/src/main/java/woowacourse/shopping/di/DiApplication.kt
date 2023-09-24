package woowacourse.shopping.di

import android.app.Application
import woowacourse.shopping.di.activity.ApplicationInstanceContainer
import woowacourse.shopping.di.module.ShoppingApplicationModule

open class DiApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        injector = AndroidDiInjector(
            applicationInstanceContainer = ApplicationInstanceContainer(),
            applicationModule = ShoppingApplicationModule(this)
        )
    }

    override fun onTerminate() {
        injector.applicationInstanceContainer.clear()

        super.onTerminate()
    }

    companion object {

        lateinit var injector: AndroidDiInjector
    }
}
