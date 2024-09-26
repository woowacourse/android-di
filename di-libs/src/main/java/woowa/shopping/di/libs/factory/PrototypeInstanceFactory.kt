package woowa.shopping.di.libs.factory

import woowa.shopping.di.libs.qualify.Qualifier

class PrototypeInstanceFactory<T : Any>(
    override val qualifier: Qualifier? = null,
    private val factory: () -> T,
) :
    InstanceFactory<T>() {
    override fun instance(): T = factory()
}
