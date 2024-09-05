package woowacourse.di

import kotlin.reflect.KClass

sealed class InjectedComponent {
    abstract val injectedClass: KClass<*>
    abstract val instance: Any?

    class InjectedSingletonComponent(
        override val injectedClass: KClass<*>,
        override val instance: Any,
    ) : InjectedComponent()

    class InjectedActivityComponent(
        override val injectedClass: KClass<*>,
        override val instance: Any,
        val activityClass: KClass<*>,
    ) : InjectedComponent()
}
