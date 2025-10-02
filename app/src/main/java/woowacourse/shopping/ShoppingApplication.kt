package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.DependencyInjector

class ShoppingApplication : Application() {
    val appContainer = AppContainer()
    val dependencyInjector by lazy { DependencyInjector(appContainer) }
}
