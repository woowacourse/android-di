package smile.di

internal class FakeDependencyRepository : DependencyRepository {
    private val instances = mutableMapOf<DependencyKey, Any>()

    override fun get(key: DependencyKey): Any? = instances[key]

    override fun save(
        key: DependencyKey,
        instance: Any,
    ) {
        instances[key] = instance
    }
}
