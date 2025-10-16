package com.daedan.di

import androidx.lifecycle.ViewModel
import com.daedan.di.qualifier.AnnotationQualifier
import com.daedan.di.qualifier.NamedQualifier
import com.daedan.di.qualifier.Qualifier
import com.daedan.di.qualifier.TypeQualifier
import com.daedan.di.scope.CreateRule

data class DependencyModule(
    val factories: List<DependencyFactory<*>>,
)

class DependencyModuleBuilder(
    val appContainerStore: AppContainerStore,
) {
    val factories = mutableListOf<DependencyFactory<*>>()

    inline fun <reified T : Annotation> annotated(): AnnotationQualifier = AnnotationQualifier(T::class)

    fun named(name: String): NamedQualifier = NamedQualifier(name)

    inline fun <reified T : ViewModel> viewModel(
        qualifier: Qualifier = TypeQualifier(T::class),
        noinline create: () -> T,
    ) {
        val createRule = CreateRule.FACTORY
        factories.add(DependencyFactory(qualifier, createRule, create))
    }

    inline fun <reified T : Any> factory(
        qualifier: Qualifier = TypeQualifier(T::class),
        noinline create: () -> T,
    ) {
        val createRule = CreateRule.FACTORY
        factories.add(DependencyFactory(qualifier, createRule, create))
    }

    inline fun <reified T : Any> single(
        qualifier: Qualifier = TypeQualifier(T::class),
        noinline create: () -> T,
    ) {
        val createRule = CreateRule.SINGLETON
        factories.add(DependencyFactory(qualifier, createRule, create))
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
