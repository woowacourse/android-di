package woowacourse.shopping.ui

import android.app.Application
import com.hyegyeong.di.DiContainer
import woowacourse.shopping.data.di.AppModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DiContainer.appModule = AppModule(this)
        DiContainer.dependencyModule = AppModule(this)
    }
}
