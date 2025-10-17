package woowacourse.shopping

import android.app.Application
import com.m6z1.moongdi.startMoong
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.di.AppContainer

class ShoppingApplication : Application() {
    private lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)

        startMoong(
            appContainer.cartProductDao,
            DefaultCartRepository::class,
            InMemoryCartRepository::class,
            DefaultProductRepository::class,
        )
    }
}
