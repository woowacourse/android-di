package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DefaultAppContainer

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DefaultAppContainer.init(this)
    }
}
