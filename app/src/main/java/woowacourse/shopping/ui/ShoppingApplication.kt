package woowacourse.shopping.ui

import android.app.Application
import com.hyegyeong.di.DiContainer
import com.hyegyeong.di.Injector
import woowacourse.shopping.data.di.AppDiModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Injector.container = DiContainer(AppDiModule(this))
    }
}