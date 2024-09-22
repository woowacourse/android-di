package com.example.sh1mj1.component

import com.example.sh1mj1.annotation.Inject
import com.example.sh1mj1.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties


data class InjectedSingletonComponent(
    val injectedClass: KClass<*>,
    val instance: Any? = null,
    val qualifier: Qualifier? = null,
) {
    fun injectableProperties(): List<KProperty1<out Any, *>> =
        instance!!::class.memberProperties.filter {
            it.hasAnnotation<Inject>()
        }
}

inline fun <reified T> singletonComponent(
    instance: T,
    qualifier: Qualifier? = null,
): InjectedSingletonComponent =
    InjectedSingletonComponent(
        injectedClass = T::class,
        instance = instance,
        qualifier = qualifier,
    )
