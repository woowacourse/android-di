package woowacourse.shopping

import android.app.Application
import com.bandal.fullmoon.FullMoonInjector
import woowacourse.shopping.common.ShoppingAppContainer

class ShoppingApplication : Application() {
    lateinit var injector: FullMoonInjector

    override fun onCreate() {
        super.onCreate()
        injector = FullMoonInjector(ShoppingAppContainer(this))
    }
}
