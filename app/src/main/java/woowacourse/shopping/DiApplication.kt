package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.AppContainer

class DiApplication : Application() {
    val appContainer = AppContainer()
}
