package woowacourse.shopping

import com.example.seogi.di.DiApplication
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DiModule

class ShoppingApplication : DiApplication(DiModule) {
    override fun onCreate() {
        super.onCreate()
        appDatabase = ShoppingDatabase.getInstance(this)
    }

    companion object {
        lateinit var appDatabase: ShoppingDatabase
            private set
    }
}
