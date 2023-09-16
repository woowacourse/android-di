package woowacourse.shopping

import android.app.Application
import com.lope.di.inject.CustomInjector
import woowacourse.shopping.di.ApplicationModule

class ShoppingApplication : Application() {

    lateinit var injector: CustomInjector
        private set

    override fun onCreate() {
        super.onCreate()
        injector = CustomInjector()
        ApplicationModule(this, injector)
    }
}
