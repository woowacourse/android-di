package woowacourse.shopping

import android.app.Application
import com.example.di.AppContainer

class App : Application() {
    val container: AppContainer = AppContainer()

    override fun onCreate() {
        super.onCreate()

        container.init(this)
    }
}
