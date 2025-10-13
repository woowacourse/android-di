package woowacourse.shopping

import android.app.Application
import com.example.di.DependencyInjector

class ShoppingApp : Application() {
    private val appContainer by lazy { AppContainer(this) }

    override fun onCreate() {
        super.onCreate()
        DependencyInjector.setInstance(appContainer)
    }
}
