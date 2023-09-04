package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.di.Injector
import woowacourse.shopping.data.di.RepositoryModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injector = Injector(listOf(RepositoryModule()))
    }

    companion object {
        lateinit var injector: Injector
    }
}