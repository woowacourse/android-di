package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.inject.AutoDependencyInjector

class ShoppingApplication : Application() {
    val autoDependencyInjector = AutoDependencyInjector()
}
