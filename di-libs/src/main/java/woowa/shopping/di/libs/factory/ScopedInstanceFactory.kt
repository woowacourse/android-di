package woowa.shopping.di.libs.factory

import woowa.shopping.di.libs.qualify.Qualifier
import kotlin.reflect.KClass

class ScopedInstanceFactory<T : Any>(
    override val qualifier: Qualifier? = null,
    val instanceClazz: KClass<T>,
    private val factory: () -> T
) : InstanceFactory<T>() {
    private var cachedInstance: T? = null
    override fun instance(): T {
        val instance = cachedInstance ?: return factory().also {
            this.cachedInstance = it
        }
        return instance
    }

    fun clear() {
        cachedInstance = null
    }
}
