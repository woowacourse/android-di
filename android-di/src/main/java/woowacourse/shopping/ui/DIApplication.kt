package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.DIModule
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

open class DIApplication : Application() {
    private var applicationModule: DIModule? = null
    private var applicationModuleClazz: KClass<out DIModule>? = null
    private var activityRetainedModuleClazz: KClass<out DIModule>? = null
    private var activityModuleClazz: KClass<out DIModule>? = null
    private var viewModelModuleClazz: KClass<out DIModule>? = null

    override fun onCreate() {
        super.onCreate()
        applicationModule = applicationModuleClazz?.let {
            it.primaryConstructor?.call(null)
        } ?: DIModule(null)
    }

    fun setModules(
        applicationModule: KClass<out DIModule>? = null,
        activityRetainedModule: KClass<out DIModule>? = null,
        activityModule: KClass<out DIModule>? = null,
        viewModelModule: KClass<out DIModule>? = null,
    ) {
        applicationModuleClazz = applicationModule
        activityRetainedModuleClazz = activityRetainedModule
        activityModuleClazz = activityModule
        viewModelModuleClazz = viewModelModule
    }

    internal fun getActivityRetainedModule(): DIModule {
        return activityRetainedModuleClazz?.let {
            it.primaryConstructor?.call(applicationModule)
        } ?: DIModule(applicationModule)
    }

    internal fun getActivityModule(activityRetainedModule: DIModule): DIModule {
        return activityModuleClazz?.let {
            it.primaryConstructor?.call(activityRetainedModule)
        } ?: DIModule(activityRetainedModule)
    }
}
