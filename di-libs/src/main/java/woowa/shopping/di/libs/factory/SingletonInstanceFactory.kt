package woowa.shopping.di.libs.factory

import woowa.shopping.di.libs.qualify.Qualifier

class SingletonInstanceFactory<T : Any>(
    override val qualifier: Qualifier? = null,
    private val factory: () -> T,
) : InstanceFactory<T>() {
    private val instance: T by lazy { factory() }
    override fun instance(): T = instance
}