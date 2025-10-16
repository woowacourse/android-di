package woowacourse.shopping

import android.app.Application
import com.m6z1.moongdi.DependencyContainer
import com.m6z1.moongdi.startMoong
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AppContainer

class ShoppingApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)

        DependencyContainer.register(ShoppingDatabase.getDatabase(this).cartProductDao())
        startMoong(
            appContainer.cartProductDao,
            DefaultCartRepository::class,
            InMemoryCartRepository::class,
            DefaultProductRepository::class,
        )
    }
}
