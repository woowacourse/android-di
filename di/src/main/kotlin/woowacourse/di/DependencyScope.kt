package woowacourse.di

import java.util.UUID

/** Identifies a kind of scope without making the container depend on lifecycle APIs. */
data class ScopeKey(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "스코프 키는 비어 있을 수 없습니다." }
    }
}

/** A cache owned and closed by the integration layer that knows the scope's lifetime. */
class DependencyScope internal constructor(
    val key: ScopeKey,
    val id: String,
) : AutoCloseable {
    private val instances: MutableMap<Any, Any> = mutableMapOf()

    @Volatile
    var isClosed: Boolean = false
        private set

    @Synchronized
    internal fun getOrCreate(
        dependencyKey: Any,
        factory: () -> Any,
    ): Any {
        check(!isClosed) { "이미 종료된 스코프입니다: ${key.value} ($id)" }
        return instances.getOrPut(dependencyKey, factory)
    }

    @Synchronized
    internal fun cachedInstanceCount(): Int = instances.size

    @Synchronized
    override fun close() {
        if (isClosed) return
        instances.clear()
        isClosed = true
    }

    companion object {
        internal fun create(
            key: ScopeKey,
            id: String = UUID.randomUUID().toString(),
        ): DependencyScope {
            require(id.isNotBlank()) { "스코프 ID는 비어 있을 수 없습니다." }
            return DependencyScope(key, id)
        }
    }
}
