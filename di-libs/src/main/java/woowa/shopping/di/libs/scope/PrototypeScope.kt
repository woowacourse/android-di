package woowa.shopping.di.libs.scope

class PrototypeScope<T : Any>(private val factory: () -> T) : LifeCycleScope<T> {
    override fun instance(): T = factory()
}