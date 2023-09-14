package woowacourse.shopping.global

import android.app.Application
import com.app.covi_di.core.DIContainer
import woowacourse.shopping.di.module.DataBaseProvider
import woowacourse.shopping.di.module.RepositoryModule

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.init(listOf(RepositoryModule), listOf(DataBaseProvider(this)))
    }
}