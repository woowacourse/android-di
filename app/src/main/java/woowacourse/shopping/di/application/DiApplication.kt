package woowacourse.shopping.di.application

import android.app.Application
import woowacourse.shopping.di.container.DiActivityModuleContainer
import woowacourse.shopping.di.module.ApplicationModule
import kotlin.reflect.full.createInstance

open class DiApplication<T : ApplicationModule>(private val applicationModuleClazz: Class<T>) :
    Application() {
    private lateinit var applicationModule: ApplicationModule
    lateinit var diContainer: DiActivityModuleContainer
        private set

    override fun onCreate() {
        super.onCreate()
        applicationModule = applicationModuleClazz.kotlin.createInstance()
        diContainer = DiActivityModuleContainer(applicationModule)
    }
}
