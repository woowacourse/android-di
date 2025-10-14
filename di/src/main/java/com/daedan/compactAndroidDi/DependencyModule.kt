package com.daedan.compactAndroidDi

import com.daedan.compactAndroidDi.qualifier.AnnotationQualifier
import com.daedan.compactAndroidDi.qualifier.NamedQualifier
import com.daedan.compactAndroidDi.qualifier.Qualifier
import com.daedan.compactAndroidDi.qualifier.TypeQualifier

data class DependencyModule(
    val factories: List<DependencyFactory<*>>,
)

class DependencyModuleBuilder(
    val appContainerStore: AppContainerStore,
) {
    val factories = mutableListOf<DependencyFactory<*>>()

    inline fun <reified T : Annotation> annotated(): AnnotationQualifier = AnnotationQualifier(T::class)

    fun named(name: String): NamedQualifier = NamedQualifier(name)

    inline fun <reified T : Any> factory(
        qualifier: Qualifier = TypeQualifier(T::class),
        noinline create: () -> T,
    ) {
        factories.add(DependencyFactory(qualifier, create))
    }

    inline fun <reified T : Any> get(qualifier: Qualifier = TypeQualifier(T::class)): T = appContainerStore.instantiate(qualifier) as T

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
