package woowacourse.shopping

import android.app.Application

class DIApplication : Application() {
    lateinit var diContainer: DIContainer

    override fun onCreate() {
        super.onCreate()
        diContainer = DIContainer()
    }
}
