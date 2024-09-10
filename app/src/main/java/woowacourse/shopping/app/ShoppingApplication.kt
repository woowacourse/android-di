package woowacourse.shopping.app

import android.app.Application
import woowacourse.shopping.di.DIInjector
import woowacourse.shopping.di.DIModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DIInjector.injectModule(DIModule(this))
    }
}
