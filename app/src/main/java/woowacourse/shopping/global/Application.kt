package woowacourse.shopping.global

import android.app.Application
import woowacourse.shopping.di.DIContainer

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.init()
    }
}