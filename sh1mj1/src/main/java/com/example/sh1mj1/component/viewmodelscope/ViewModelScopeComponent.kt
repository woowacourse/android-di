package com.example.sh1mj1.component.viewmodelscope

import com.example.sh1mj1.annotation.Inject
import com.example.sh1mj1.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

data class ViewModelScopeComponent<T : Any>(
    val injectedClass: KClass<T>,
    val instance: T,
    val qualifier: Qualifier? = null,
) {
    fun injectableProperties(): List<KProperty1<out Any, *>> =
        instance::class.memberProperties.filter {
            it.hasAnnotation<Inject>()
        }
}

data class ViewModelScopeComponent2<T : Any>(
    val injectedClass: KClass<T>,
    val instance: T,
    val qualifier: Qualifier? = null,
) {
    fun injectableProperties(): List<KProperty1<out Any, *>> =
        instance::class.memberProperties.filter {
            it.hasAnnotation<Inject>()
        }
}

inline fun <reified T : Any> viewModelScopeComponent(
    instance: T,
    qualifier: Qualifier? = null,
): ViewModelScopeComponent<T> =
    ViewModelScopeComponent(
        injectedClass = T::class,
        instance = instance,
        qualifier = qualifier,
    )
