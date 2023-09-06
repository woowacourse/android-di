package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.Injector

class FakeApplication : Application() {
    companion object {
        lateinit var injector: Injector
    }
}
