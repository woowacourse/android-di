package woowacourse.shopping

import android.app.Application
import android.content.Context
import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.dependency.DependencyContainer
import woowacourse.shopping.di.DependencyProvider

class DiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val dependencyContainer = DependencyContainer.getSingletonInstance()
        val injector = Injector.getSingletonInstance()
        dependencyContainer.addInstance(Context::class, emptyList(), applicationContext)
        dependencyContainer.addInstance(CartProductDao::class, emptyList(), ShoppingDatabase.getDatabase(applicationContext).cartProductDao())
        injector.providerContainer.addProvider(DependencyProvider())
    }
}
