package woowacourse.shopping

import android.app.Application
import com.daedan.di.AppContainerStore
import com.daedan.di.DiComponent
import woowacourse.shopping.di.dataModule
import woowacourse.shopping.di.dateFormatterModule
import woowacourse.shopping.di.repositoryModule
import woowacourse.shopping.di.viewModelModule

class MainApplication :
    Application(),
    DiComponent {
    override val appContainerStore = AppContainerStore()

    override fun onCreate() {
        super.onCreate()
        register(
            dataModule(this),
            repositoryModule(),
            viewModelModule(),
            dateFormatterModule(),
        )
    }
}
