package woowacourse.shopping.di.application

import android.app.Application
import woowacourse.shopping.di.container.DefaultInstanceContainer
import woowacourse.shopping.di.container.InstanceContainer
import woowacourse.shopping.di.module.ActivityModule
import woowacourse.shopping.di.module.ApplicationModule
import woowacourse.shopping.di.module.ViewModelModule
import kotlin.reflect.full.primaryConstructor

open class DIApplication(
    private val applicationModuleClazz: Class<out ApplicationModule>,
    private val activityModuleClazz: Class<out ActivityModule>,
    private val viewModelModuleClazz: Class<out ViewModelModule>,
) : Application() {
    private lateinit var applicationModule: ApplicationModule
    private val instanceContainer: InstanceContainer = DefaultInstanceContainer(listOf())

    override fun onCreate() {
        super.onCreate()

        val primaryConstructor =
            applicationModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()

        applicationModule = primaryConstructor.call(this, instanceContainer)
        applicationModule.inject(this)
    }

    fun getActivityModule(): ActivityModule {
        val primaryConstructor =
            activityModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()
        return primaryConstructor.call(this, applicationModule, instanceContainer)
    }

    fun getViewModelModule(): ViewModelModule {
        val primaryConstructor =
            viewModelModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()
        return primaryConstructor.call(applicationModule, instanceContainer)
    }
}
