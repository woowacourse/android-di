package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.ViewModelFactory

class ShoppingApplication : Application() {
    private val dependencyInjector: DependencyInjector by lazy {
        DependencyInjector(AppContainer(this))
    }

    val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(dependencyInjector)
    }
}
