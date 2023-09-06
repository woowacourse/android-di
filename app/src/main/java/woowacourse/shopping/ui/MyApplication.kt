package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.di.Injector
import woowacourse.shopping.data.di.RepositoryModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Injector.modules = listOf(RepositoryModule())
    }
}