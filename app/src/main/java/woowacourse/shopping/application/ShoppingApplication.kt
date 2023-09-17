package woowacourse.shopping.application

import android.app.Application
import com.bignerdranch.android.koala.Injector
import woowacourse.shopping.data.ShoppingContainer

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        injector = Injector(
            ShoppingContainer(applicationContext),
        )
    }

    companion object {
        lateinit var injector: Injector
    }
}
