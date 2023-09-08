package woowacourse.shopping.global

import android.app.Application
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.RepositoryModule

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.init(listOf(RepositoryModule))
    }
}