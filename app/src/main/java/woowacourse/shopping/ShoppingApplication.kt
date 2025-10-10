package woowacourse.shopping

import android.app.Application
import android.content.Context

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: ShoppingApplication
        val context: Context
            get() = instance.applicationContext
    }
}
