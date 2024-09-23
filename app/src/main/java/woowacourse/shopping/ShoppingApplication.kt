package woowacourse.shopping

import com.example.seogi.di.DiApplication
import com.example.seogi.di.DiContainer
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DiModule

class ShoppingApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()
        appDatabase = ShoppingDatabase.getInstance(this)
        module = DiModule
        diContainer = DiContainer(DiModule, this)
    }

    companion object {
        lateinit var appDatabase: ShoppingDatabase
            private set
    }
}
