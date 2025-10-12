package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.ViewModelFactory

class ShoppingApplication : Application() {
    private val container = AppContainer()
    val injectViewModelFactory by lazy { ViewModelFactory(container) }
}
