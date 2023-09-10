package woowacourse.shopping

import android.app.Application
import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdi.HasDiContainer
import woowacourse.shopping.di.DiApplicationModule

class AppApplication : Application(), HasDiContainer {
    override lateinit var diContainer: DiContainer

    override fun onCreate() {
        super.onCreate()

        diContainer = DiApplicationModule(applicationContext)
    }
}
