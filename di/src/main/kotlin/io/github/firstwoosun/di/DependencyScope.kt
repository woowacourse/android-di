package io.github.firstwoosun.di

class DependencyScope(
    val kind: ScopeKind,
    private var parent: DependencyScope? = null,
) : AutoCloseable {
    private val instances = mutableMapOf<DependencyKey, Any>()
    private val resolving = mutableSetOf<DependencyKey>()
    private val children = mutableListOf<DependencyScope>()

    var isClosed: Boolean = false
        private set

    internal val instanceCount: Int
        get() = instances.size

    internal val childCount: Int
        get() = children.size

    fun openChild(kind: ScopeKind): DependencyScope {
        checkOpen()

        return DependencyScope(
            kind = kind,
            parent = this,
        ).also(children::add)
    }

    fun findOwner(kind: ScopeKind): DependencyScope {
        checkOpen()

        if (kind == this.kind) {
            return this
        }

        return parent?.findOwner(kind)
            ?: error("접근할 수 없는 스코프입니다: $kind")
    }

    fun getOrCreate(
        key: DependencyKey,
        create: () -> Any,
    ): Any {
        checkOpen()

        instances[key]?.let { return it }

        check(resolving.add(key)) { "순환 의존성 발생: $key" }

        return try {
            create().also {
                checkOpen()
                instances[key] = it
            }
        } finally {
            resolving.remove(key)
        }
    }

    fun checkOpen() {
        check(!isClosed) { "이미 종료된 스코프: $kind" }
    }

    override fun close() {
        if (isClosed) return

        isClosed = true

        children.toList().forEach { it.close() }
        children.clear()

        instances.clear()
        resolving.clear()

        parent?.children?.remove(this)
        parent = null
    }
}
