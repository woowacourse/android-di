package woowacourse.shopping.di

typealias ClassQualifier = Pair<DependencyType, AnnotationQualifier>

class DefaultDependencyContainer(
    private val dependencies: MutableMap<ClassQualifier, ImplementationClass<*>> =
        mutableMapOf(),
    private val cachedInstances: MutableMap<ClassQualifier, DependencyInstance> =
        mutableMapOf()
) : DependencyContainer {
    override fun <T : DependencyInstance> getInstance(
        dependency: DependencyType,
        qualifier: AnnotationQualifier,
    ): T? = cachedInstances[dependency to qualifier] as T?

    override fun <T : Any> getImplement(
        dependency: DependencyType,
        qualifier: AnnotationQualifier,
    ): ImplementationClass<T>? = dependencies[dependency to qualifier] as? ImplementationClass<T>

    override fun <T : Any> setDependency(
        dependency: DependencyType,
        implementation: ImplementationClass<T>,
        qualifier: AnnotationQualifier,
    ) {
        dependencies[dependency to qualifier] = implementation
    }

    override fun setInstance(
        dependency: DependencyType,
        instance: DependencyInstance,
        qualifier: AnnotationQualifier,
    ) {
        if (dependencies.contains(dependency to qualifier)) {
            cachedInstances[dependency to qualifier] = instance
        }
    }
}
