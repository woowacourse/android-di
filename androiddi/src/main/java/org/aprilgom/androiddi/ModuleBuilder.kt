package org.aprilgom.androiddi

import android.content.Context
import kotlin.reflect.KClass

class ModuleBuilder {
    val providers: MutableMap<NamedKClass, Provider<*>> = mutableMapOf()
    fun build(): Module = Module(providers)

    fun exists(clazz: KClass<*>): Boolean {
        val namedKClass = NamedKClass(clazz)
        return exists(namedKClass)
    }

    fun exists(name: String, clazz: KClass<*>): Boolean {
        val namedKClass = NamedKClass(name, clazz)
        return exists(namedKClass)
    }

    fun exists(namedKClass: NamedKClass) =
        providers.containsKey(namedKClass)
}
