package woowacourse.shopping

import android.app.Application
import com.example.di.AppContainer

class ShoppingApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        appContainer =
            AppContainer().apply {
                ShoppingDIModule(this@ShoppingApplication).register(this)
            }
    }
}
