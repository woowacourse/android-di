package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.AppContainerStore

class MainApplication : Application() {
    val appContainer = AppContainer()
    val appContainerStore = AppContainerStore(appContainer)
}
