package woowacourse.shopping.global

import android.app.Application
import woowacourse.shopping.di.core.DIContainer
import woowacourse.shopping.di.module.RepositoryModule

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.init(listOf(RepositoryModule))
    }
}