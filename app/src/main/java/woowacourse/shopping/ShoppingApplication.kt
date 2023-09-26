package woowacourse.shopping

import com.example.bbottodi.di.DiApplication
import woowacourse.shopping.di.ApplicationModule

class ShoppingApplication : DiApplication() {

    override fun onCreate() {
        module = { context -> ApplicationModule(context) }
        super.onCreate()
    }
}
