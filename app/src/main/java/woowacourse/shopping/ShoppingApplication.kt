package woowacourse.shopping

import android.app.Application
import com.woowacourse.bunadi.dsl.modules
import woowacourse.shopping.ui.common.di.module.DaoModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        modules {
            module(DaoModule(this@ShoppingApplication))
        }
    }
}
