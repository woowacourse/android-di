package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.ui.injection.repository.RepositoryModule

class DIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RepositoryModule.initLifeCycle(this)
    }
}
