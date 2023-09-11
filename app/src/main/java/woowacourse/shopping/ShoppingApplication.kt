package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.RepositoryContainer

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        database = ShoppingDatabase.getDatabase(this)
        injector = Injector(RepositoryContainer())
    }

    companion object {
        lateinit var injector: Injector
        lateinit var database: ShoppingDatabase
    }
}
