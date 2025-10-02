package woowacourse.shopping

import android.app.Application

class ShoppingApplication : Application() {
    val appContainer by lazy { AppContainer() }
}
