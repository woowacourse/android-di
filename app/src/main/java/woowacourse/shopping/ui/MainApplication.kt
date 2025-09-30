package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.di.AppContainer

class MainApplication : Application() {
    val appContainer = AppContainer()
}