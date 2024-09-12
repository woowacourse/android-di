package woowacourse.shopping.ui.injection

import woowacourse.shopping.ui.injection.repository.RepositoryDI
import woowacourse.shopping.ui.injection.repository.RepositoryModule
import kotlin.reflect.KClass

object ModuleRegistry {
    private val moduleMap =
        mapOf<KClass<*>, KClass<out Module<*, *>>>(RepositoryDI::class to RepositoryModule::class)

    fun <type : Any> getModuleForType(key: KClass<out type>): KClass<out Module<*, *>>? {
        return this.moduleMap[key]
    }

    fun moduleTypes(): Set<KClass<*>> {
        return moduleMap.keys
    }
}
