package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.FakeRepositoryModule
import woowacourse.shopping.di.Injector

class FakeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injector = Injector(listOf(FakeRepositoryModule))
    }

    companion object {
        lateinit var injector: Injector
    }
}
