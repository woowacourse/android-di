package woowacourse.di

import kotlin.reflect.KClass

class Scope internal constructor(
    internal val container: Container,
    val type: ScopeType,
    parent: Scope?,
) : AutoCloseable {
    private var parentScope: Scope? = parent
    private val instances = mutableMapOf<Container.Key, Any>()

    var isClosed: Boolean = false
        private set

    fun resolve(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any = container.resolve(this, type, qualifier)

    fun <T : Any> create(type: KClass<T>): T = container.create(this, type)

    override fun close() = container.closeScope(this)

    internal val parent: Scope? get() = parentScope

    internal val cachedInstanceCount: Int get() = instances.size

    internal fun cache(
        key: Container.Key,
        create: () -> Any,
    ): Any = instances.getOrPut(key, create)

    internal fun remove(key: Container.Key) {
        instances.remove(key)
    }

    internal fun isDescendantOf(ancestor: Scope): Boolean {
        var current = parentScope
        while (current != null) {
            if (current === ancestor) return true
            current = current.parentScope
        }
        return false
    }

    internal fun depth(): Int {
        var result = 0
        var current = parentScope
        while (current != null) {
            result++
            current = current.parentScope
        }
        return result
    }

    internal fun find(type: ScopeType): Scope? {
        var current: Scope? = this
        while (current != null) {
            current.ensureOpen()
            if (current.type == type) return current
            current = current.parentScope
        }
        return null
    }

    internal fun ensureOpen() {
        check(!isClosed) { "${type.name} 스코프가 이미 종료되었습니다." }
    }

    internal fun markClosed() {
        instances.clear()
        parentScope = null
        isClosed = true
    }
}
