package woowacourse.shopping.application

import android.app.Application
import android.content.Context

class ShoppingApplication : Application() {

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
