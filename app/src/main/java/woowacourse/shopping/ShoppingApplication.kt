package woowacourse.shopping

import com.example.yennydi.application.DiApplication
import com.example.yennydi.di.DependencyProvider
import woowacourse.shopping.di.ShoppingApplicationModule

class ShoppingApplication : DiApplication() {
    override val dependencyProvider: DependencyProvider = ShoppingApplicationModule(this)

    override fun onCreate() {
        super.onCreate()
    }
}
