package woowacourse.shopping.di.provider

import woowacourse.shopping.di.container.DependencyInstance
import woowacourse.shopping.di.container.ImplementationClass

interface DependencyProvider {
    fun <T : DependencyInstance> getInstance(): T?

    fun <T : Any> getImplementation(): ImplementationClass<T>?

    fun <T : Any> setDependency(implementationClass: ImplementationClass<T>)

    fun setInstance(instance: DependencyInstance)

    fun deleteInstance()
}
