package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.container.AppContainer

class MyApplication : Application() {
    val container: AppContainer = AppContainer()
}
