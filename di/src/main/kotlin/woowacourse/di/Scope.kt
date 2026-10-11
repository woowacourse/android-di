package woowacourse.di

import kotlin.reflect.KClass

data class ScopeType(
    val name: String,
) {
    init {
        require(name.isNotBlank()) { "스코프 이름은 비어 있을 수 없습니다." }
    }

    companion object {
        val Singleton: ScopeType = ScopeType("singleton")
    }
}

internal data class ScopeKey(
    val type: ScopeType,
    val id: String,
)

class ScopedContainer internal constructor(
    private val container: DIContainer,
    private val activeScopes: Map<ScopeType, ScopeKey>,
    private val ownedScope: ScopeKey,
) : DIResolver,
    AutoCloseable {
    override fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
    ): T = container.get(type, qualifier, activeScopes)

    override fun <T : Any> create(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
    ): T = container.create(type, qualifier, activeScopes)

    override fun openScope(
        type: ScopeType,
        id: String,
    ): ScopedContainer = container.openScope(type, id, activeScopes)

    override fun close() {
        container.closeScope(ownedScope)
    }
}
