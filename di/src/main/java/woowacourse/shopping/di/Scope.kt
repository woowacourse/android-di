package woowacourse.shopping.di

class Scope(
    val id: ScopeKey,
    val parentKey: ScopeKey? = null,
) {
    private val instances = mutableMapOf<DependencyKey<*>, Any>()

    fun <T : Any> get(key: DependencyKey<T>): T? =
        instances[key]?.let { key.type.java.cast(it) }

    fun <T : Any> put(key: DependencyKey<T>, instance: T) {
        instances[key] = instance
    }

    fun clear() {
        instances.clear()
    }
}
