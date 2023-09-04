package woowacourse.shopping.application

import android.app.Application
import android.content.Context
import woowacourse.shopping.data.Injector

class ShoppingApplication : Application() {

    val injector: Injector = Injector

    init {
        instance = this
    }

    companion object {
        lateinit var instance: ShoppingApplication
        fun getApplicationContext(): Context {
            return instance.applicationContext
        }
    }
}
