package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.cast

class Scope internal constructor(
    private val factories: Map<KClass<*>, () -> Any>,
) {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun <T : Any> resolve(type: KClass<T>): T {
        val instance =
            instances.getOrPut(type) {
                val factory =
                    checkNotNull(factories[type]) {
                        "${type.qualifiedName}에 등록된 의존성이 없습니다."
                    }
                factory()
            }
        return type.cast(instance)
    }
}
