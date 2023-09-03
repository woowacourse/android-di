package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.AppContainer

class MyApplication : Application() {
    val appContainer = AppContainer()
}