package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.di.DiModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        diContainer = DiContainer(DiModule)
    }

    companion object {
        lateinit var diContainer: DiContainer
            private set
    }
}
