package woowacourse.shopping.ui

import android.app.Application
import android.content.Context
import woowacourse.shopping.annotation.ApplicationContext
import woowacourse.shopping.module.DIActivityModule
import woowacourse.shopping.module.DIActivityRetainedModule
import woowacourse.shopping.module.DIApplicationModule
import woowacourse.shopping.module.DIVIewModelModule
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

open class DIApplication : Application() {
    private lateinit var applicationModule: DIApplicationModule
    private var applicationModuleClazz: KClass<out DIApplicationModule>? = null
    private var activityRetainedModuleClazz: KClass<out DIActivityRetainedModule>? = null
    private var activityModuleClazz: KClass<out DIActivityModule>? = null
    private var viewModelModuleClazz: KClass<out DIVIewModelModule>? = null

    override fun onCreate() {
        super.onCreate()
        applicationModule = applicationModuleClazz?.let {
            it.primaryConstructor?.call()
        } ?: DIApplicationModule()

        val key = (Context::class to ApplicationContext())
        applicationModule.addInstance(key, this)
    }

    fun setModules(
        applicationModule: KClass<out DIApplicationModule>? = null,
        activityRetainedModule: KClass<out DIActivityRetainedModule>? = null,
        activityModule: KClass<out DIActivityModule>? = null,
        viewModelModule: KClass<out DIVIewModelModule>? = null,
    ) {
        applicationModuleClazz = applicationModule
        activityRetainedModuleClazz = activityRetainedModule
        activityModuleClazz = activityModule
        viewModelModuleClazz = viewModelModule
    }

    internal fun getActivityRetainedModule(): DIActivityRetainedModule {
        return activityRetainedModuleClazz?.let {
            it.primaryConstructor?.call(applicationModule)
        } ?: DIActivityRetainedModule(applicationModule)
    }

    internal fun getActivityModule(activityRetainedModule: DIActivityRetainedModule): DIActivityModule {
        return activityModuleClazz?.let {
            it.primaryConstructor?.call(activityRetainedModule)
        } ?: DIActivityModule(activityRetainedModule)
    }

    internal fun getViewModelModule(activityRetainedModule: DIActivityRetainedModule): DIVIewModelModule {
        return viewModelModuleClazz?.let {
            it.primaryConstructor?.call(activityRetainedModule)
        } ?: DIVIewModelModule(activityRetainedModule)
    }
}
