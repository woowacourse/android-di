package woowacourse.shopping

import android.app.Application
import com.woowa.di.injection.ModuleRegistry
import woowacourse.shopping.di.DaoDI
import woowacourse.shopping.di.DaoModule
import woowacourse.shopping.di.RepositoryModule
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
