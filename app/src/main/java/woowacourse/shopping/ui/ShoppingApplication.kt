package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.RoomDBCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.di.DefaultContainer
import woowacourse.shopping.data.di.Injector

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Injector.container = DefaultContainer
        initContainer()
    }

    private fun initContainer() {
        DefaultContainer.addInstance(
            DefaultProductRepository()
        )
        DefaultContainer.addInstance(
            ShoppingDatabase.getDatabase(applicationContext).cartProductDao()
        )
        DefaultContainer.addInstance(
            InMemoryCartRepository()
        )
        DefaultContainer.addInstance(
            Injector.inject<RoomDBCartRepository>()
        )
    }
}