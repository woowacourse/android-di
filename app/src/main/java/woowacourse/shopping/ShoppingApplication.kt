package woowacourse.shopping

import com.example.seogi.di.DiApplication
import com.example.seogi.di.DiContainer
import woowacourse.shopping.di.DiModule

class ShoppingApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()
        module = DiModule
        diContainer = DiContainer(DiModule, this@ShoppingApplication)
    }
}
