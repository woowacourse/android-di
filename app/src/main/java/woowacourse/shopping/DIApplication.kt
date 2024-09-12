package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.ui.injection.dao.DaoModule
import woowacourse.shopping.ui.injection.repository.RepositoryModule

class DIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DaoModule.initLifeCycle(this)
        RepositoryModule.initLifeCycle(this)
    }
}
