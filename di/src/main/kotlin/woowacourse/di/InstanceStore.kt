package woowacourse.di

internal class InstanceStore {
    private val instances: MutableMap<DependencyKey, Any> = mutableMapOf()

    operator fun get(key: DependencyKey): Any? = instances[key]

    operator fun set(
        key: DependencyKey,
        instance: Any,
    ) {
        instances[key] = instance
    }
}
