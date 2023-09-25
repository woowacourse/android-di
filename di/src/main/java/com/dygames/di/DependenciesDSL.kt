package com.dygames.di

import kotlin.reflect.KType
import kotlin.reflect.typeOf

inline fun <reified T : Any> Qualifier.provider(noinline init: () -> T) {
    providers[typeOf<T>()] = init
}

inline fun <reified T : Any> Qualifier.provider(type: KType) {
    constructors[typeOf<T>()] = type
}

inline fun <reified T : Any> Dependencies.provider(noinline init: () -> T) {
    if (!qualifiers.containsKey(null)) qualifiers[null] = Qualifier()
    qualifiers[null]?.providers?.set(typeOf<T>(), init)
}

inline fun <reified T : Any> Dependencies.provider(type: KType) {
    if (!qualifiers.containsKey(null)) qualifiers[null] = Qualifier()
    qualifiers[null]?.constructors?.set(typeOf<T>(), type)
}

fun Dependencies.qualifier(annotation: Annotation? = null, init: Qualifier.() -> Unit) {
    qualifiers[annotation] = Qualifier().apply {
        init()
    }
}

fun dependencies(
    init: Dependencies.() -> Unit
) {
    DependencyInjector.dependencies = Dependencies().apply {
        init()
    }
}
