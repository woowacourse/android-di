package woowacourse.shopping

import android.app.Application
import woowacourse.di.Injector
import woowacourse.shopping.di.shoppingInjector

class ShoppingApplication : Application() {
    val injector: Injector by lazy { shoppingInjector(this) }
}
