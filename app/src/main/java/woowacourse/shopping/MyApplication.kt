package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.ApplicationContextProvider

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ApplicationContextProvider.setupApplicationContext(this)
    }
}
