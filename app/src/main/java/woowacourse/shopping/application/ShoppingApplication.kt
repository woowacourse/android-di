package woowacourse.shopping.application

import android.app.Application
import android.content.Context
import woowacourse.shopping.data.ShoppingContainer

class ShoppingApplication : Application() {

    lateinit var container: ShoppingContainer

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        container = ShoppingContainer()
    }

    companion object {
        lateinit var instance: ShoppingApplication
        fun getApplicationContext(): Context {
            return instance.applicationContext
        }
    }
}
