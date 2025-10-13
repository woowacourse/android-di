package woowacourse.shopping

import android.app.Application
import android.content.Context
import com.example.di.AppContainer
import com.example.di.provide
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.RepositoryModule

class App : Application() {
    val container: AppContainer = AppContainer()

    override fun onCreate() {
        super.onCreate()

        container.provide<Context>(this)
        container.provideModule(DatabaseModule::class)
        container.provideModule(RepositoryModule::class)
    }
}
