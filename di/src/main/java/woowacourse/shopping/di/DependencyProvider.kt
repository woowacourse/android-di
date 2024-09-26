package woowacourse.shopping.di

import woowacourse.shopping.di.container.DependencyInstance
import woowacourse.shopping.di.container.ImplementationClass

interface DependencyProvider {
    fun <T : DependencyInstance> getInstance(): T?

    fun <T : Any> getImplement(): ImplementationClass<T>?

    fun <T : Any> setDependency(implementationClass: ImplementationClass<T>)

    fun setInstance(instance: DependencyInstance)

    fun deleteInstance()
}
