package woowacourse.shopping.di

class DependencyScope internal constructor(
    private val parent: DependencyScope? = null,
) {
    private val dependencies = mutableMapOf<Any, Any>()

    internal fun find(key: Any): Any? = dependencies[key] ?: parent?.find(key)

    internal fun put(
        key: Any,
        dependency: Any,
    ) {
        dependencies[key] = dependency
    }

    internal fun keys(): Set<Any> = dependencies.keys + parent?.keys().orEmpty()

    fun clear() {
        dependencies.clear()
    }
}
