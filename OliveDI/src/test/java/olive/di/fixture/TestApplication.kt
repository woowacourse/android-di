package olive.di.fixture

import android.app.Application
import olive.di.DIContainer
import olive.di.DIModule
import kotlin.reflect.KClass

class TestApplication : Application() {
    private val diModules: List<KClass<out DIModule>> by lazy {
        listOf(ActivityScopeTestModule::class, ViewModelScopeTestModule::class)
    }

    override fun onCreate() {
        super.onCreate()
        DIContainer.injectApplication(this, this::class)
        DIContainer.injectModules(diModules)
    }
}
