package woowacourse.shopping.di.application

import android.app.Application
import woowacourse.shopping.di.container.DiActivityModuleContainer
import woowacourse.shopping.di.module.ApplicationModule
import kotlin.reflect.full.primaryConstructor

open class DiApplication<T : ApplicationModule>(private val applicationModuleClazz: Class<T>) :
    Application() {
    private lateinit var applicationModule: ApplicationModule
    lateinit var diContainer: DiActivityModuleContainer
        private set

    override fun onCreate() {
        super.onCreate()
        val primaryConstructor = applicationModuleClazz.kotlin.primaryConstructor
            ?: throw NullPointerException("ApplicationModule은 매개변수가 없는 주생성자가 있어야 합니다")
        applicationModule = primaryConstructor.call()
        diContainer = DiActivityModuleContainer(applicationModule)
    }
}
