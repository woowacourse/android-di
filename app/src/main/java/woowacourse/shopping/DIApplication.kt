package woowacourse.shopping

import android.app.Application
import com.woowa.di.injection.ModuleRegistry
import woowacourse.shopping.di.dao.DaoDI
import woowacourse.shopping.di.dao.DaoModule
import woowacourse.shopping.di.repository.RepositoryModule
import woowacourse.shopping.di.repository.RepositoryDI

class DIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ModuleRegistry.registerModule(RepositoryDI::class, RepositoryModule::class)
        ModuleRegistry.registerModule(
            DaoDI::class, DaoModule::class
        )
        DaoModule.initLifeCycle(this)
        RepositoryModule.initLifeCycle(this)
    }
}
