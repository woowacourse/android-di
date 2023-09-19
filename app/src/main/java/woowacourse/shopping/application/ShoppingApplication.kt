package woowacourse.shopping.application

import android.app.Application
import com.created.customdi.StartDi
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        StartDi {
            registerContext(this@ShoppingApplication.applicationContext)
            registerModule(DatabaseModule)
            registerModule(RepositoryModule)
        }
    }
}
