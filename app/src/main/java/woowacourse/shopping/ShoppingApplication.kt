package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication : Application() {
    private val db by lazy { ShoppingDatabase.initialize(applicationContext) }
    private val cartProductDao by lazy { db.cartProductDao() }
    private lateinit var componentRegister: ComponentRegister

    override fun onCreate() {
        super.onCreate()
        componentRegister = ComponentRegister(applicationContext, cartProductDao)
        componentRegister.initialize()
    }
}
