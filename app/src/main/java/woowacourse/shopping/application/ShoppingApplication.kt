package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.di.container.DefaultContainer
import woowacourse.shopping.di.container.ShoppingContainer

class ShoppingApplication : Application() {
    val appContainer: ShoppingContainer = DefaultContainer()
}
