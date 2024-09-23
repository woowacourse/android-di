package woowacourse.shopping.di

interface DependencyProvider {
    fun <T : DependencyInstance> getInstance(): T?

    fun <T : Any> getImplement(): ImplementationClass<T>?

    fun <T : Any> setDependency(implementationClass: ImplementationClass<T>)

    fun setInstance(instance: DependencyInstance)

    fun deleteInstance()
}
