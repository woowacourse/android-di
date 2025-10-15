package woowacourse.shopping

import android.app.Application
import com.on.di_library.di.DiContainer

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        DiContainer.setContext(this)
        DiContainer.getAnnotatedModules()
    }
}
