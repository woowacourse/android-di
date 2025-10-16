package woowacourse.shopping

import android.app.Application
import com.example.di_v2.DIContainer

class ShoppingApplication : Application() {
    lateinit var appContainer: DIContainer
        private set

    override fun onCreate() {
        super.onCreate()

        appContainer = DIContainer()
        val module = ShoppingDIModule(applicationContext)
        module.register(appContainer)

        appContainer.printBindings()
    }
}
