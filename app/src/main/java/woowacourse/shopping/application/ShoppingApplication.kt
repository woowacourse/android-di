package woowacourse.shopping.application

import android.app.Application
import com.example.di.DIInjector

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupModule()
    }

    private fun setupModule() {
        DIInjector.injectModule(ApplicationModule(this))
    }
}
