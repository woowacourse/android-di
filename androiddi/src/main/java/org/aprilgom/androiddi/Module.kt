package org.aprilgom.androiddi

class Module(
    val providers: Map<NamedKClass, Provider<*>>,
) {
    operator fun plus(module: Module): Module {
        val newProviders = providers + module.providers
        return Module(newProviders)
    }
}
