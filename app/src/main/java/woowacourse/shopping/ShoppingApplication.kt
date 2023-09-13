package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.cart.CartProductDao
import woowacourse.shopping.di.module.RepositoryModule
import woowacourse.shopping.otterdi.Injector

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initDependencies()
    }

    private fun initDependencies() {
        cartProductDao = ShoppingDatabase.getDatabase(this@ShoppingApplication).cartProductDao()
        injector = Injector(RepositoryModule)
    }

    companion object {
        lateinit var injector: Injector
        lateinit var cartProductDao: CartProductDao
    }
}
