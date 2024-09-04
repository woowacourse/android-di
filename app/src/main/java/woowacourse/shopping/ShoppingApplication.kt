package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.di.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDi()
    }

    private fun initializeDi() {
        RepositoryModule.install()
    }
}
