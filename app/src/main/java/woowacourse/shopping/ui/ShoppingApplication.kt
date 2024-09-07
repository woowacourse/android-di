package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.di.AppModule
import woowacourse.shopping.di.InstanceContainer
import woowacourse.shopping.di.DefaultAppModule

class ShoppingApplication : Application() {
    private val appModule: AppModule by lazy {
        DefaultAppModule(appContext = this)
    }

    override fun onCreate() {
        super.onCreate()
        instanceContainer = InstanceContainer(listOf(appModule))
    }

    companion object {
        lateinit var instanceContainer: InstanceContainer
            private set
    }
}
