package woowacourse.di

import kotlin.reflect.KClass

object DIScopeManager {
    private val scopedInstances =
        mutableMapOf<ScopeType, MutableMap<Pair<KClass<*>, KClass<out Annotation>?>, Any>>()

    fun getInstance(
        scope: ScopeType,
        key: Pair<KClass<*>, KClass<out Annotation>?>,
    ): Any? {
        return scopedInstances[scope]?.get(key)
    }

    fun putInstance(
        scope: ScopeType,
        key: Pair<KClass<*>, KClass<out Annotation>?>,
        instance: Any,
    ) {
        val map = scopedInstances.getOrPut(scope) { mutableMapOf() }
        map[key] = instance
    }

    fun clearScope(scope: ScopeType) {
        scopedInstances.remove(scope)
    }
}
