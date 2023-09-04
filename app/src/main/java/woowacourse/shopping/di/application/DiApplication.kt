package woowacourse.shopping.di.application

import android.app.Application
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.module.ApplicationModule
import javax.annotation.CheckReturnValue
import kotlin.reflect.full.primaryConstructor

open class DiApplication<T : ApplicationModule>(private val applicationModuleClazz: Class<T>) :
    Application() {
    override fun onCreate() {
        super.onCreate()
        val primaryConstructor = applicationModuleClazz.kotlin.primaryConstructor
            ?: throw IllegalArgumentException("주 생성자를 찾을 수 없습니다: ${applicationModuleClazz.simpleName}")
        applicationModule = primaryConstructor.call()
    }

    companion object {
        lateinit var applicationModule: ApplicationModule
            private set

        private val injectorMap: MutableMap<Int, Injector?> = mutableMapOf()

        @CheckReturnValue
        fun hasInjectorForInstance(instanceHashCode: Int): Boolean {
            return injectorMap[instanceHashCode] != null
        }

        fun getInjectorForInstance(instanceHashCode: Int): Injector {
            requireNotNull(injectorMap[instanceHashCode]) { "인젝터가 존재하지 않습니다. 먼저 초기화해주세요." }
            return injectorMap[instanceHashCode]!!
        }

        fun addInjectorForInstance(instanceHashCode: Int, injector: Injector) {
            injectorMap[instanceHashCode] = injector
        }

        fun removeInjectorForInstance(instanceHashCode: Int) {
            injectorMap[instanceHashCode] = null
        }
    }
}
