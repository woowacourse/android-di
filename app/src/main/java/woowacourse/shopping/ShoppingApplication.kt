package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer

class ShoppingApplication : Application() {
    val appContainer = AppContainer()
}
