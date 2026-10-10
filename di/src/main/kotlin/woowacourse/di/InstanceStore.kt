package woowacourse.di

internal class InstanceStore {
    private val instances = mutableMapOf<DependencyKey, Any>()

    fun get(key: DependencyKey): Any? = instances[key]

    fun put(
        key: DependencyKey,
        instance: Any,
    ) {
        instances[key] = instance
    }

    fun clear() {
        instances.clear()
    }
}
