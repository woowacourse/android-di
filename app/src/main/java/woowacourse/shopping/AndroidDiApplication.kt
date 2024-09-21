package woowacourse.shopping

import android.app.Application
import com.example.di.DependencyInjector
import com.example.di.DiContainer
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.di.DatabaseModule

class AndroidDiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDatabase()
        initializeDependencies()
    }

    private fun initializeDatabase() {
        ShoppingDatabase.init(this)
    }

    private fun initializeDependencies() {
        val diContainer = DiContainer()
        diContainer.addModule(DatabaseModule)
        injector = DependencyInjector(diContainer)
    }

    companion object {
        lateinit var injector: DependencyInjector
            private set
    }
}
