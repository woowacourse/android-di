package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.di.repositoryModule
import woowacourse.shopping.di.viewModelModule
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.util.autoDI.AutoDI
import woowacourse.shopping.util.autoDI.autoDIModule.autoDIModule

class DIApplication : Application() {
    override fun onCreate() {
        initAutoDI()
        super.onCreate()
    }

    private fun initAutoDI() {
        AutoDI {
            registerModule(repositoryModule)
            registerModule(viewModelModule)
        }
    }
}
