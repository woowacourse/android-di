package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.ShoppingContainer

class ShoppingApplication : Application() {
    lateinit var container: ShoppingContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = ShoppingContainer(applicationContext)
    }
}
