package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AutoDi
import woowacourse.shopping.di.ShoppingContainer

class ShoppingApplication : Application() {
    private val container = ShoppingContainer()

    val autoDi = AutoDi(container)
}
