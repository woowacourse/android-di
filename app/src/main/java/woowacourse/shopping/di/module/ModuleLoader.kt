package woowacourse.shopping.di.module

import android.content.Context
import com.zzang.di.DIContainer

class ModuleLoader(private val context: Context) {
    fun initializeModules() {
        initDatabase()
        initRepository()
    }

    private fun initDatabase() {
        val databaseModule = DatabaseModule(context)
        DIContainer.loadModule(databaseModule)
    }

    private fun initRepository() {
        val repositoryModule = RepositoryModule()
        DIContainer.loadModule(repositoryModule)
    }
}
