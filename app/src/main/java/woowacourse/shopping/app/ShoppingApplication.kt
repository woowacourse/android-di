package woowacourse.shopping.app

import android.app.Application
import woowacourse.shopping.app.di.AppContainerImpl
import woowacourse.shopping.di.Container

class ShoppingApplication : Application() {
    val container: Container by lazy {
        AppContainerImpl(this)
    }
}
