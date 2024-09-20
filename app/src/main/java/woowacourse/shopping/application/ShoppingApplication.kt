package woowacourse.shopping.application

import android.app.Application
import com.example.di.DIInjector

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DIInjector.injectModule(AppModule(this))
    }
}
