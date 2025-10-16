package com.daedan.di.dsl

import androidx.lifecycle.ViewModel
import com.daedan.di.AppContainerStore
import com.daedan.di.DependencyFactory
import com.daedan.di.DependencyModule
import com.daedan.di.qualifier.AnnotationQualifier
import com.daedan.di.qualifier.NamedQualifier
import com.daedan.di.qualifier.Qualifier
import com.daedan.di.qualifier.TypeQualifier
import com.daedan.di.scope.CreateRule
import com.daedan.di.scope.Scope
import com.daedan.di.scope.TypeScope

@DependencyModuleDSL
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
        val createRule = CreateRule.SINGLE
        factories.add(DependencyFactory(qualifier, createRule, create))
    }

    inline fun <reified T : Any> scope(
        qualifier: Scope = TypeScope(T::class),
        block: ScopeDependencyModuleBuilder.() -> Unit,
    ) {
        block(ScopeDependencyModuleBuilder(qualifier, this))
    }

    inline fun <reified T : Any> get(qualifier: Qualifier = TypeQualifier(T::class)): T = appContainerStore.instantiate(qualifier) as T

    fun build(): DependencyModule = DependencyModule(factories)
}
