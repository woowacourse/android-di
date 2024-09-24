package woowacourse.shopping

import com.woowacourse.di.DiApplication
import com.woowacourse.di.Module
import woowacourse.shopping.data.di.ApplicationModule

class ShoppingApplication : DiApplication() {
    override val module: Module = ApplicationModule(this)

    override fun onCreate() {
        super.onCreate()
    }
}
