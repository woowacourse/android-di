package woowacourse.shopping

import com.example.di.DIApplication
import woowacourse.shopping.di.DefaultAppModule

class ShoppingApplication : DIApplication(DefaultAppModule) {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: ShoppingApplication
    }
}
