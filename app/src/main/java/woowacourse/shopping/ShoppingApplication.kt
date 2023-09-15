package woowacourse.shopping

import android.app.Application
import com.woosuk.scott_di.startDI
import woowacourse.shopping.di.DefaultModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        startDI {
            loadModule(DefaultModule)
        }
    }

    companion object {
        lateinit var instance: ShoppingApplication
    }
}
