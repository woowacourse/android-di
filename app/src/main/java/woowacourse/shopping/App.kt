package woowacourse.shopping

import android.app.Application
import android.content.Context
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.RepositoryModule

class App : Application() {
    val container: Container by lazy { Container() }

    override fun onCreate() {
        super.onCreate()
        container.bind(type = Context::class, provider = { this })
        container.installModule(DatabaseModule)
        container.installModule(RepositoryModule)
    }
}
