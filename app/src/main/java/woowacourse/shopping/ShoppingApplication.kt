package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication : Application() {
    private val db by lazy { ShoppingDatabase.initialize(applicationContext) }
    private val cartProductDao by lazy { db.cartProductDao() }

    override fun onCreate() {
        super.onCreate()
        DependencyInjector.initialize(cartProductDao)
    }
}
