package woowacourse.shopping

import android.app.Application
import com.example.di.DependencyInjector
import woowacourse.shopping.data.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyInjector.initialize(RepositoryModule(this))
    }
}
