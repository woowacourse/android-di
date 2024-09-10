package woowa.shopping.di.libs.scope

class SingletonScope<T : Any>(private val factory: () -> T) : LifeCycleScope<T> {
    private val instance: T by lazy { factory() }

    override fun instance(): T = instance
}