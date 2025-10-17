package woowacourse.peto.di.annotation

import woowacourse.peto.di.DependencyKey
import java.util.concurrent.ConcurrentHashMap

class ScopedInstanceProvider {
    private val instance = ConcurrentHashMap<Scope, ConcurrentHashMap<DependencyKey, Any>>()

    fun get(
        scope: Scope,
        key: DependencyKey,
    ): Any? {
        return instance[scope]?.get(key)
    }

    fun computeIfAbsent(
        scope: Scope,
        key: DependencyKey,
        newInstance: Any,
    ) {
        instance.computeIfAbsent(scope) { ConcurrentHashMap() }[key] = newInstance
    }

    fun remove(scope: Scope) {
        instance.remove(scope)
    }
}
