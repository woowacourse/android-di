package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.di.AppDependencyContainer
import com.hyegyeong.di.Injector

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Injector.container = AppDependencyContainer(this)
    }
}