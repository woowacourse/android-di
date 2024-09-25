package woowacourse.shopping

import android.app.Application
import com.woowacourse.di.DependencyContainer
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication : Application() {
    private val db by lazy { ShoppingDatabase.initialize(applicationContext) }
    private val cartProductDao by lazy { db.cartProductDao() }
    private lateinit var componentRegister: ComponentRegister

    override fun onCreate() {
        super.onCreate()
        dependencyContainer = DependencyContainer()
        componentRegister = ComponentRegister(applicationContext, cartProductDao)
        componentRegister.initialize()
    }

    companion object {
        lateinit var dependencyContainer: DependencyContainer
            private set
    }
}
