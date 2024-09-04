package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DIContainer

class ShoppingApplication : Application() {
    val diContainer: DIContainer by lazy { DIContainer() }
}
