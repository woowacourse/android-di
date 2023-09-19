package woowacourse.shopping.di

import android.app.Application
import kotlin.reflect.full.primaryConstructor

open class DIApplication(
    private val applicationModuleClazz: Class<out ApplicationModule>,
    private val activityModuleClazz: Class<out ActivityModule>,
    private val viewModelModuleClazz: Class<out ViewModelModule>,
) : Application() {

    lateinit var applicationModule: ApplicationModule

    override fun onCreate() {
        super.onCreate()

        val primaryConstructor =
            applicationModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()

        applicationModule = primaryConstructor.call(this)
        applicationModule.inject(this)
    }

    fun getActivityModule(): ActivityModule {
        val primaryConstructor =
            activityModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()
        return primaryConstructor.call(this, applicationModule)
    }

    fun getViewModelModule(): ViewModelModule {
        val primaryConstructor =
            viewModelModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()
        return primaryConstructor.call(this, getActivityModule())
    }
}
