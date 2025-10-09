package woowacourse.shopping.di

import woowacourse.shopping.domain.DependencyModule
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties

class DependencyInjector(
    modules: List<DependencyModule>,
) {
    private val providers = mutableMapOf<KType, () -> Any?>()
    private val instances = mutableMapOf<KType, Any>()

    init {
        modules.forEach {
            registerModule(it)
        }
    }

    private fun registerModule(module: DependencyModule) {
        module::class.memberProperties.forEach { property ->
            val kType = property.returnType
            providers[kType] = { property.getter.call(module) }
        }
    }

    fun get(type: KType): Any =
        instances[type] ?: run {
            val instance =
                requireNotNull(providers[type]?.invoke()) {
                    val className = (type.classifier as? KClass<*>)?.simpleName ?: type
                    DEPENDENCY_NOT_FOUND.format(className)
                }

            instances[type] = instance
            return instance
        }

    companion object {
        private const val DEPENDENCY_NOT_FOUND = "%s 타입의 의존성이 등록되어있지 않습니다."
    }
}
