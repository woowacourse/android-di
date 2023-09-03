package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.di.container.DefaultContainer
import woowacourse.shopping.di.container.ShoppingContainer
import woowacourse.shopping.di.injector.Injector

class ShoppingApplication : Application() {
    private val appContainer: ShoppingContainer = DefaultContainer()
    val injector = Injector(appContainer)
}
