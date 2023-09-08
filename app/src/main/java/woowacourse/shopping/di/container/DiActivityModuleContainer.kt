package woowacourse.shopping.di.container

import woowacourse.shopping.di.module.ActivityModule
import woowacourse.shopping.di.module.ApplicationModule
import kotlin.reflect.full.primaryConstructor

class DiActivityModuleContainer(private val applicationModule: ApplicationModule) {
    private val moduleMap: MutableMap<Int, ActivityModule?> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityModule> provideActivityModule(
        newOwnerHashCode: Int,
        oldOwnerHashCode: Int?,
        clazz: Class<T>,
    ): T {
        val moduleToProvide =
            moduleMap[oldOwnerHashCode] ?: createFromOwnerHashCode(newOwnerHashCode, clazz)
        moduleMap[newOwnerHashCode] = moduleToProvide
        return moduleToProvide as T
    }

    private fun <T : ActivityModule> createFromOwnerHashCode(
        ownerHashCode: Int,
        clazz: Class<T>,
    ): T {
        val primaryConstructor = clazz.kotlin.primaryConstructor
            ?: throw NullPointerException("액티비티 모듈의 주 생성자는 애플리케이션 모듈만 매개변수로 선언되어있어야 합니다.")
        val module = primaryConstructor.call(applicationModule)
        moduleMap[ownerHashCode] = module
        return module
    }

    fun removeModule(ownerHashCode: Int) {
        moduleMap[ownerHashCode] = null
    }
}
