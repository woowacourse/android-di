package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainerImpl
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.ViewModelFactory

class ShoppingApplication : Application() {
    private val dependencyInjector: DependencyInjector by lazy {
        DependencyInjector(AppContainerImpl(this))
    }

    val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(dependencyInjector)
    }
}
