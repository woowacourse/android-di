package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DiContainer

class MyApplication : Application() {
    val diContainer: DiContainer by lazy { DiContainer(this) }

    override fun onCreate() {
        super.onCreate()
    }
}