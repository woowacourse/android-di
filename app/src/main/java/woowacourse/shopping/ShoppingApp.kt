package woowacourse.shopping

import android.app.Application
import com.example.di.DependencyInjector
import com.example.di.scope.AppScopeHandler

class ShoppingApp : Application() {
    private val appContainer by lazy { AppContainer(this) }

    override fun onCreate() {
        super.onCreate()
        AppScopeHandler
        DependencyInjector.setInstance(appContainer)
    }
}
