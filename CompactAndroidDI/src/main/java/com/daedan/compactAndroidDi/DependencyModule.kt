package com.daedan.compactAndroidDi

data class DependencyModule(
    val factories: List<DependencyFactory<*>>,
)

class DependencyModuleBuilder(
    val appContainerStore: AppContainerStore,
) {
    val factories = mutableListOf<DependencyFactory<*>>()

    inline fun <reified T : Any> factory(
        name: String? = null,
        noinline create: () -> T,
    ) {
        factories.add(DependencyFactory(Qualifier(T::class, name), create))
    }

    inline fun <reified T : Any> get(name: String? = null): T = appContainerStore.instantiate(Qualifier(T::class, name)) as T

    fun build(): DependencyModule = DependencyModule(factories)
}

fun DiApplication.module(block: DependencyModuleBuilder.() -> Unit): DependencyModule = module(appContainerStore, block)

fun module(
    container: AppContainerStore,
    block: DependencyModuleBuilder.() -> Unit,
): DependencyModule {
    val builder = DependencyModuleBuilder(container)
    block(builder)
    return builder.build()
}
