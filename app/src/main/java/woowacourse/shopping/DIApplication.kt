package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.ui.injection.RepositoryModule

class DIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RepositoryModule.setLifeCycle(this)
    }
}
