package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.LocalModule
import woowacourse.shopping.di.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDi()
    }

    private fun initializeDi() {
        LocalModule.install(this@ShoppingApplication)
        RepositoryModule.install()
    }
}
