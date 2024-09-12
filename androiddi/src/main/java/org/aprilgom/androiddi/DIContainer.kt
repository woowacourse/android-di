package org.aprilgom.androiddi

import android.content.Context
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DIContainer{
    val modules = mutableListOf<Module>()
    fun provide(name: String, clazz: KClass<*>): Any {
        val namedKClass = NamedKClass(name, clazz)
        return provide(namedKClass)
    }

    private fun provide(namedKClass: NamedKClass): Any {
        val providers = modules.flatMap { it.providers.entries }.associate { it.key to it.value }
        return providers[namedKClass]?.get()
            ?: throw IllegalArgumentException("name: ${namedKClass.name} clazz: ${namedKClass.clazz} is not provided")
    }

    fun inject() {
        modules.forEach { it.inject() }
    }
}
/*
class DIContainer(
    val context: Context,
    modules: List<Module>
) {
    init{
        _globalModule = _globalModule?.plus(modules.reduce { acc, module -> acc + module })
    }
    fun inject() { globalModule.inject() }
    //private val module = modules.reduce { acc, module -> acc + module }
    companion object{
        private var _globalModule: Module? = null
        val globalModule:Module get() = _globalModule!!
    }
}
 */
