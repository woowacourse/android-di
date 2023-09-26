package woowacourse.shopping.hasydi

import android.app.Application
import kotlin.reflect.KClass

abstract class DiApplication(
    private val appModule: Module = DefaultModule(),
    private val activityRetainedModule: List<Pair<KClass<*>, Module>> = listOf(),
) : Application() {

    lateinit var injector: Injector

    override fun onCreate() {
        super.onCreate()
        appModule.context = this
        initApplicationModule()
        initActivityRetainedModule()
    }

    private fun initApplicationModule() {
        injector = Injector(DiContainer(appModule))
    }

    private fun initActivityRetainedModule() {
        activityRetainedModule.forEach {
            injector.addActivityRetainedModule(it.first, it.second)
        }
    }
}
