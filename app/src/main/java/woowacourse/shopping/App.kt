package woowacourse.shopping

import android.app.Application
import com.example.di.AppContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.RepositoryModule
import woowacourse.shopping.domain.CartRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        AppContainer.provide<CartProductDao>(ShoppingDatabase.getInstance(this).cartProductDao())
        AppContainer.provideModule(RepositoryModule::class)
    }
}
