package woowacourse.di

import kotlin.reflect.KClass

object DIContainer {
    private val providers = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>?>, () -> Any>()

    fun <T : Any> register(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        creator: () -> T,
    ) {
        providers[clazz to qualifier] = creator
    }

    fun <T : Any> get(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scope: ScopeType = ScopeType.Singleton,
    ): T {
        val key = clazz to qualifier
        val creator =
            providers[key]
                ?: throw Exception("No provider for $clazz with qualifier $qualifier")

        DIScopeManager.getInstance(scope, key)?.let { return it as T }

        val instance = creator()
        DIScopeManager.putInstance(scope, key, instance)
        return instance as T
    }
}
