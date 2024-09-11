package woowacourse.shopping.application

import android.app.Application

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        com.example.di.DIInjector.injectModule(AppModule(this))
    }
}
