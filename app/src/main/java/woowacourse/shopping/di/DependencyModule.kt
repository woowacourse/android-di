package woowacourse.shopping.di

import kotlin.reflect.KClass

data class DependencyModule(
    val factories: List<DependencyFactory<*>>,
)

class DependencyModuleBuilder(
    val appContainerStore: AppContainerStore,
) {
    val factories = mutableListOf<DependencyFactory<*>>()

    inline fun <reified T : Any> factory(noinline create: () -> T) {
        factories.add(DependencyFactory(T::class, create))
    }

    inline fun <reified T : Any> get(): T = appContainerStore.instantiate(T::class, saveToCache = false) as T

    fun build(): DependencyModule = DependencyModule(factories)
}

fun DiApplication.module(block: DependencyModuleBuilder.() -> Unit): DependencyModule {
    val builder = DependencyModuleBuilder(appContainerStore)
    block(builder)
    return builder.build()
}
