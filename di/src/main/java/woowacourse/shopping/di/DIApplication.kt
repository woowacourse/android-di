package woowacourse.shopping.di

import android.app.Application
import kotlin.reflect.full.primaryConstructor

open class DIApplication(
    private val applicationModuleClazz: Class<out ApplicationModule>,
    private val activityModuleClazz: Class<out ActivityModule>,
    private val viewModelModuleClazz: Class<out ViewModelModule>,
) : Application() {
    lateinit var applicationModule: ApplicationModule
    private val modules: MutableMap<String, Any> = mutableMapOf()

    override fun onCreate() {
        super.onCreate()

        val primaryConstructor =
            applicationModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()

        applicationModule = primaryConstructor.call(this)
        applicationModule.inject(this)
    }

    fun getActivityModule(): ActivityModule {
        val qualifiedName = requireNotNull(ActivityModule::class.qualifiedName)
        val module = modules[qualifiedName] ?: run {
            val primaryConstructor =
                activityModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()
            primaryConstructor.call(this, applicationModule).also { modules[qualifiedName] = it }
        }
        return module as ActivityModule
    }

    fun getViewModelModule(): ViewModelModule {
        val qualifiedName = requireNotNull(ViewModelModule::class.qualifiedName)
        val module = modules[qualifiedName] ?: run {
            val primaryConstructor =
                viewModelModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()
            primaryConstructor.call(this, getActivityModule()).also { modules[qualifiedName] = it }
        }
        return module as ViewModelModule
    }
}
