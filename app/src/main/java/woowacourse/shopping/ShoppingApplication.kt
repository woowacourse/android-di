package woowacourse.shopping

import android.app.Application
import com.cksckckcks.di.AutoDi
import woowacourse.shopping.di.ShoppingContainer

class ShoppingApplication : Application() {
    lateinit var container: ShoppingContainer
        private set

    val autoDi by lazy { AutoDi(container.diContainer) }

    override fun onCreate() {
        super.onCreate()
        container = ShoppingContainer(applicationContext)
    }
}
