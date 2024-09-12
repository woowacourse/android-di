package org.aprilgom.androiddi

class DIContainerBuilder {
    lateinit var moduleBuilders: List<ModuleBuilder>

    fun build(): DIContainer{
        val modules = moduleBuilders.map { it.build() }
        DIContainer.modules.addAll(modules)
        return DIContainer
    }
}
