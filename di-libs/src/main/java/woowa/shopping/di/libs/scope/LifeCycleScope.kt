package woowa.shopping.di.libs.scope

interface LifeCycleScope<T : Any> {
    fun instance(): T
}