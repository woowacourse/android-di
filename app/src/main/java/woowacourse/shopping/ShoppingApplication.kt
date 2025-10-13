package woowacourse.shopping

import android.app.Application
import com.example.di.DependencyProvider
import woowacourse.shopping.data.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyProvider.initialize(RepositoryModule(this))
    }
}
