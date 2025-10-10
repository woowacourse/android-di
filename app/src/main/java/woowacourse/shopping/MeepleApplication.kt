package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer

class MeepleApplication : Application() {
    val appContainer by lazy { AppContainer(this) }
}
