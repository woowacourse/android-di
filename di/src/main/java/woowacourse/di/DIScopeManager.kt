package woowacourse.di

object DIScopeManager {
    private val singletonInstances = mutableMapOf<Any, Any>()
    private val scopedInstances = mutableMapOf<String, MutableMap<Any, Any>>()

    fun getInstance(
        scope: ScopeType,
        scopeKey: String,
        dependencyKey: Any,
    ): Any? {
        return when (scope) {
            ScopeType.Singleton -> singletonInstances[dependencyKey]
            ScopeType.Activity, ScopeType.ViewModel -> scopedInstances[scopeKey]?.get(dependencyKey)
        }
    }

    fun putInstance(
        scope: ScopeType,
        scopeKey: String,
        dependencyKey: Any,
        instance: Any,
    ) {
        when (scope) {
            ScopeType.Singleton -> singletonInstances[dependencyKey] = instance
            ScopeType.Activity, ScopeType.ViewModel -> {
                scopedInstances.computeIfAbsent(scopeKey) { mutableMapOf() }[dependencyKey] =
                    instance
            }
        }
    }

    fun clearScope(scopeKey: String) {
        scopedInstances.remove(scopeKey)
    }
}
