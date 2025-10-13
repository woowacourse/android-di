package woowacourse.shopping

import android.app.Application
import com.shopping.di.InjectContainer
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.RepositoryModule
import woowacourse.shopping.di.ViewModelModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        InjectContainer.init(
            DatabaseModule(applicationContext),
            RepositoryModule(),
            ViewModelModule(),
        )
    }
}
