package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.di.DiModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appDatabase = ShoppingDatabase.getInstance(this)
        diContainer =
            DiContainer(DiModule, this.packageName, this.packageCodePath, this.classLoader)
    }

    companion object {
        lateinit var diContainer: DiContainer
            private set

        lateinit var appDatabase: ShoppingDatabase
            private set
    }
}
