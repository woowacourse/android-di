package woowacourse.shopping

import android.app.Application
import com.example.di.Injector
import com.example.di.SourceContainer
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
        val sourceContainer =
            SourceContainer()
                .apply {
                    initApplication(this@AndroidDiApplication)
                    addModule(DatabaseModule)
                }
        Injector.init(sourceContainer)
    }
}
