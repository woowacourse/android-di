package woowacourse.shopping

import android.app.Application
import com.angrypig.autodi.AutoDI
import woowacourse.shopping.di.androidModule
import woowacourse.shopping.di.localStorageModule
import woowacourse.shopping.di.repositoryModule
import woowacourse.shopping.di.viewModelModule

class DIApplication : Application() {
    override fun onCreate() {
        initAutoDI()
        super.onCreate()
    }

    private fun initAutoDI() {
        AutoDI {
            registerApplicationContext(applicationContext)
            registerModule(repositoryModule)
            registerModule(viewModelModule)
            registerModule(localStorageModule)
            registerModule(androidModule)
        }
    }
}
