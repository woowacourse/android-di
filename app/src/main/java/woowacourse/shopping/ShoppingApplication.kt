package woowacourse.shopping

import com.woosuk.scott_di_android.DiApplication
import woowacourse.shopping.di.DefaultModule

class ShoppingApplication : DiApplication(DefaultModule) {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: ShoppingApplication
    }
}
