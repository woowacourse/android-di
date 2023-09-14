package woowacourse.shopping.di.sgDi

import kotlin.reflect.KClass

object DependencyContainer {
    private val _repositories: MutableMap<KClass<*>, Any> = mutableMapOf()
    val repositories: Map<KClass<*>, Any> get() = _repositories.toMap()

    private val _qualifiedRepository: MutableMap<String, Any> = mutableMapOf()
    val qualifiedRepository: Map<String, Any> get() = _qualifiedRepository.toMap()

    fun add(type: KClass<*>, instance: Any) {
        _repositories[type] = instance
        // 싱글톤 처리도 해줘야함
    }

    fun add(qualifier: String, instance: Any) {
        _qualifiedRepository[qualifier] = instance
        // 싱글톤 처리도 해줘야함
    }
}
