package woowacourse.shopping

import com.woowacourse.di.DiApplication
import woowacourse.shopping.data.di.ApplicationModule

class ShoppingApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun install() {
        ApplicationModule(this).install()
    }
}
