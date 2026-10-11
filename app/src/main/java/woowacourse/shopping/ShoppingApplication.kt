package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DIContainer

class ShoppingApplication : Application() {
    val diContainer = DIContainer()

    override fun onCreate() {
        super.onCreate()
        registerShoppingDependencies(
            diContainer = diContainer,
            application = this,
        )
    }
}
