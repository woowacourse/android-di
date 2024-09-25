package woowacourse.shopping

import android.app.Application
import com.woowacourse.di.DependencyInjector
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication : Application() {
    private val db by lazy { ShoppingDatabase.initialize(applicationContext) }
    private val cartProductDao by lazy { db.cartProductDao() }
    private lateinit var componentRegister: ComponentRegister

    override fun onCreate() {
        super.onCreate()
        dependencyInjector = DependencyInjector()
        componentRegister = ComponentRegister(applicationContext, cartProductDao)
        componentRegister.initialize()
    }

    companion object {
        lateinit var dependencyInjector: DependencyInjector
            private set
    }
}
