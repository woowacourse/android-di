package woowacourse.shopping.ui

import android.app.Application
import io.hyemdooly.di.Container
import woowacourse.shopping.ui.di.DaoModule
import woowacourse.shopping.ui.di.ProductsModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        inject()
    }

    private fun inject() {
        val daoModule = DaoModule(applicationContext)
        val products = ProductsModule()

        Container.addInstances(products)
        Container.addInstances(daoModule)
    }
}
