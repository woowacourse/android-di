package woowacourse.shopping.di

import android.app.Application
import com.re4rk.arkdi.DiContainer

open class DIApplication : Application() {
    val diContainer: DiContainer by lazy {
        DiApplicationModule(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        diContainer.inject(this)
    }
}
