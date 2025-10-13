package woowacourse.shopping

import android.app.Application
import com.on.di_library.di.DiContainer.getAnnotatedModules
import woowacourse.shopping.di.ApplicationContextProvider

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        getAnnotatedModules(this)
        ApplicationContextProvider.setupApplicationContext(this)
    }
}
