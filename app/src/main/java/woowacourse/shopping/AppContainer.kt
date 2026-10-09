package woowacourse.shopping

import smile.di.DependencyKey
import smile.di.DependencyRepository

class AppContainer : DependencyRepository {
    private val instances = mutableMapOf<DependencyKey, Any>()

    override fun get(key: DependencyKey): Any? = instances[key]

    override fun save(
        key: DependencyKey,
        instance: Any,
    ) {
        instances[key] = instance
    }
}
