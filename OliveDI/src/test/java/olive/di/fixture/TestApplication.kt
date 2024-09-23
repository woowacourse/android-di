package olive.di.fixture

import android.app.Application
import olive.di.DIContainer

class TestApplication : Application() {
    private val diModules = listOf(ActivityScopeTestModule::class ,ViewModelScopeTestModule::class)

    val diContainer: DIContainer by lazy {
        DIContainer(this, this::class, diModules)
    }
}
