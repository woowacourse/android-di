package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.RepositoryModule

class ShoppingApplication : Application() {
    val container = AppContainer()
}
