package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.InjectorViewModelFactory

class ShoppingApplication : Application() {
    val appContainer = AppContainer()
    val injectorViewModelFactory by lazy { InjectorViewModelFactory(appContainer) }
}
