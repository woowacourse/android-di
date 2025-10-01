package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.di.AppContainer

class ShoppingApplication : Application() {
    val container by lazy { AppContainer() }
}
