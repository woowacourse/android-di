package woowacourse.shopping.di

import woowacourse.shopping.di.container.DependencyInstance
import woowacourse.shopping.di.container.ImplementationClass

class ComponentDependencyProvider : DependencyProvider {
    private var implementationClass: ImplementationClass<*>? = null
    private var dependencyInstance: DependencyInstance? = null

    override fun <T : DependencyInstance> getInstance(): T? = dependencyInstance as? T

    override fun <T : Any> getImplement(): ImplementationClass<T>? =
        implementationClass as? ImplementationClass<T>

    override fun <T : Any> setDependency(implementationClass: ImplementationClass<T>) {
        this.implementationClass = implementationClass
    }

    override fun setInstance(instance: DependencyInstance) {
        dependencyInstance = instance
    }

    override fun deleteInstance() {
        dependencyInstance = null
    }
}
