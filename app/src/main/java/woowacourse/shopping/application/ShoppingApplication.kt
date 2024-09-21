package woowacourse.shopping.application

import android.app.Application
import com.example.di.Injector

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupModule()
    }

    private fun setupModule() {
        Injector.injectModule(ApplicationModule(this))
    }
}
