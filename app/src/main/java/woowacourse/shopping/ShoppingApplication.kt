package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.ContainerBuilder
import woowacourse.shopping.di.installAllBindings

class ShoppingApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        val builder = ContainerBuilder()
        installAllBindings(builder)
        container = builder.build()
    }
}
