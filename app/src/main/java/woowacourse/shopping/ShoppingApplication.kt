package woowacourse.shopping

import android.app.Application
import com.m6z1.moongdi.AutoDIViewModelFactory
import woowacourse.shopping.di.AppContainer

class ShoppingApplication : Application() {
    lateinit var appContainer: AppContainer
    val viewModelFactory: AutoDIViewModelFactory<AppContainer> by lazy {
        AutoDIViewModelFactory()
    }

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }
}
