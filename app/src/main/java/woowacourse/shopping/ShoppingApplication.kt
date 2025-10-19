package woowacourse.shopping

import android.app.Application
import com.example.di.DependencyContainer
import woowacourse.shopping.di.DateFormatterModule
import woowacourse.shopping.di.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyContainer.initialize(this, RepositoryModule(this), DateFormatterModule(this))
    }
}
