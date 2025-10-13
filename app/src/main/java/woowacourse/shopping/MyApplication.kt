package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.ApplicationContextProvider
import woowacourse.shopping.di.DiContainer.getAnnotatedModules

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        getAnnotatedModules(this)
        ApplicationContextProvider.setupApplicationContext(this)
    }
}
