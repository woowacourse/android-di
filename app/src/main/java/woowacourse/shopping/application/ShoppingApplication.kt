package woowacourse.shopping.application

import com.example.di.DIApplication
import com.example.di.DIModule
import woowacourse.shopping.module.ApplicationModule

class ShoppingApplication : DIApplication() {
    override val module: DIModule = ApplicationModule(this)
}
