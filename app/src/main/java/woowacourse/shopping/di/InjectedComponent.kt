package woowacourse.shopping.di

import kotlin.reflect.KClass

sealed class InjectedComponent {
    abstract val injectedClass: KClass<*>
    abstract val instance: Any?
    abstract val qualifier: Qualifier?

    class InjectedSingletonComponent(
        override val injectedClass: KClass<*>,
        override val instance: Any? = null,
        override val qualifier: Qualifier? = null,
    ) : InjectedComponent()

    class InjectedActivityComponent(
        override val injectedClass: KClass<*>,
        override val instance: Any? = null,
        val activityClass: KClass<*>,
        override val qualifier: Qualifier? = null,
    ) : InjectedComponent()
}
