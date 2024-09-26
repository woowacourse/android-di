package org.aprilgom.androiddi

class DIContainerBuilder {
    lateinit var moduleBuilders: List<ModuleBuilder>

    fun build(): DIContainer {
        val modules = moduleBuilders.map { it.build() }
        val providers = modules.flatMap { it.providers.entries }.associate { it.key to it.value } as MutableMap<NamedKClass, Provider<*>>
        val diContainer = DIContainer(providers)
        return diContainer
    }
}
