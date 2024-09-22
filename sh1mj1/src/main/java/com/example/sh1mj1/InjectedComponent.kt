package com.example.sh1mj1

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

sealed class InjectedComponent {
    abstract val injectedClass: KClass<*>
    abstract val instance: Any?
    abstract val qualifier: Qualifier?

    data class InjectedSingletonComponent(
        override val injectedClass: KClass<*>,
        override val instance: Any? = null,
        override val qualifier: Qualifier? = null,
    ) : InjectedComponent() {
        fun injectableProperties(): List<KProperty1<out Any, *>> =
            instance!!::class.memberProperties.filter {
                it.hasAnnotation<Inject>()
            }
    }

    data class InjectedActivityComponent(
        override val injectedClass: KClass<*>,
        override val instance: Any? = null,
        val activityClass: KClass<*>,
        override val qualifier: Qualifier? = null,
    ) : InjectedComponent()
}

inline fun <reified T> singletonComponent(
    instance: T,
    qualifier: Qualifier? = null,
): InjectedComponent.InjectedSingletonComponent =
    InjectedComponent.InjectedSingletonComponent(
        injectedClass = T::class,
        instance = instance,
        qualifier = qualifier,
    )
