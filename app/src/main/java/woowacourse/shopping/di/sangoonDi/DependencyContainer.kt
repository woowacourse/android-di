package woowacourse.shopping.di.sangoonDi

import kotlin.reflect.KClass

object DependencyContainer {
    private val _repositories: MutableMap<KClass<*>, Any> = mutableMapOf()
    val repositories: Map<KClass<*>, Any> get() = _repositories.toMap()

    fun add(type: KClass<*>, instance: Any) {
        _repositories[type] = instance
        // 싱글톤 처리도 해줘야함
    }
}
