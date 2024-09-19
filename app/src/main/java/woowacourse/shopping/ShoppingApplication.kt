package woowacourse.shopping

import com.example.seogi.di.DiApplication
import com.example.seogi.di.DiContainer
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DiModule

class ShoppingApplication : DiApplication(DiModule) {
    override fun onCreate() {
        super.onCreate()
        appDatabase = ShoppingDatabase.getInstance(this)
        diContainer = DiContainer(DiModule)
    }

    companion object {
        lateinit var appDatabase: ShoppingDatabase
            private set

        lateinit var diContainer: DiContainer
            private set
    }
}
