package woowacourse.shopping.shoppingapp

import android.app.Application
import woowacourse.shopping.shoppingapp.di.AppModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppModule.setInstance()
    }
}
