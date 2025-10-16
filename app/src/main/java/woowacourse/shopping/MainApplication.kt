package woowacourse.shopping

import com.daedan.di.DiApplication
import woowacourse.shopping.di.dataModule
import woowacourse.shopping.di.repositoryModule
import woowacourse.shopping.di.viewModelModule

class MainApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()
        register(
            dataModule(),
            repositoryModule(),
            viewModelModule(),
        )
    }
}
