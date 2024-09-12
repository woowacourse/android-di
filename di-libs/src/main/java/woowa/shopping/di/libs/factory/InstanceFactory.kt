package woowa.shopping.di.libs.factory

import woowa.shopping.di.libs.qualify.Qualifier

abstract class InstanceFactory<T : Any> {
    open val qualifier: Qualifier? = null
    abstract fun instance(): T
}
