package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DependencyInjector {
    private val providers = mutableMapOf<KClass<*>, () -> Any>()

    fun <T : Any> register(
        clazz: KClass<T>,
        provider: () -> T,
    ) {
        providers[clazz] = provider
    }

    fun <T : Any> resolve(clazz: KClass<T>): T =
        providers[clazz]?.invoke() as? T
            ?: createInstance(clazz).also { instance ->
                providers[clazz] = { instance }
            }

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor =
            requireNotNull(clazz.primaryConstructor) { "${clazz.simpleName}에 public 생성자가 없습니다." }

        return constructor.parameters
            .associateWith { param ->
                requireNotNull(param.type.classifier as? KClass<*>) {
                    "타입 정보를 알 수 없습니다: $param"
                }.let(::resolve)
            }.let(constructor::callBy)
    }
}
