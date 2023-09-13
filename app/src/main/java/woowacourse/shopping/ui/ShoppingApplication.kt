package woowacourse.shopping.ui

import android.app.Application
import com.hyegyeong.di.DefaultContainer
import com.hyegyeong.di.Injector
import woowacourse.shopping.data.RoomDBCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase

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