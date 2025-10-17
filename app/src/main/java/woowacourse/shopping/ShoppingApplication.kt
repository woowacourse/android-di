package woowacourse.shopping

import RepositoryModule
import android.app.Application
import com.yrsel.di.DependencyContainer
import woowacourse.shopping.di.DatabaseModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyContainer.init(RepositoryModule(), DatabaseModule(applicationContext))
    }
}
