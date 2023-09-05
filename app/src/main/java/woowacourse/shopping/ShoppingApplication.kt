package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.inject.CustomInjector

class ShoppingApplication : Application() {

    lateinit var inject: CustomInjector
        private set

    override fun onCreate() {
        super.onCreate()
        inject = CustomInjector()
    }
}
