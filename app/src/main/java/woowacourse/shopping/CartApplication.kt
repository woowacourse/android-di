package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DefaultDependencies
import woowacourse.shopping.di.DependencyInjector

class CartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyInjector.dependencies = DefaultDependencies
    }
}
