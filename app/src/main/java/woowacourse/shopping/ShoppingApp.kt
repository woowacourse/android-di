package woowacourse.shopping

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import woowacourse.shopping.module.databaseModule
import woowacourse.shopping.module.dateFormatterModule
import woowacourse.shopping.module.singletonRepositoryModule
import woowacourse.shopping.module.viewModelModule
import woowacourse.shopping.module.viewModelScopeRepositoryModule

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShoppingApp)
            androidLogger(level = Level.DEBUG)
            modules(myAppModules)
        }
    }
}

val myAppModules = module {
    includes(
        databaseModule,
        singletonRepositoryModule,
        viewModelScopeRepositoryModule,
        dateFormatterModule,
        viewModelModule
    )
}
