package woowacourse.shopping

import android.app.Application
import com.woowacourse.bunadi.dsl.modules
import woowacourse.shopping.ui.common.di.module.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        modules {
            module(RepositoryModule(this@ShoppingApplication))
        }
    }
}
