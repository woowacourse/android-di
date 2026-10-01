package woowacourse.shopping

import woowacourse.shopping.di.DependencyKey
import woowacourse.shopping.di.DependencyRepository

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
