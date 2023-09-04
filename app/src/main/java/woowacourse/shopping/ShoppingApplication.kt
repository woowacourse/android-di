package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.inject.CustomInject

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        inject = CustomInject()
    }

    companion object {
        lateinit var inject: CustomInject
            private set
    }
}
