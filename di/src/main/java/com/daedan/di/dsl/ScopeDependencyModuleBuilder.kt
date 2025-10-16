package com.daedan.di.dsl

import com.daedan.di.DependencyFactory
import com.daedan.di.qualifier.Qualifier
import com.daedan.di.qualifier.TypeQualifier
import com.daedan.di.scope.CreateRule
import com.daedan.di.scope.Scope

@DependencyModuleDSL
class ScopeDependencyModuleBuilder(
    val scope: Scope,
    val dependencyModuleBuilder: DependencyModuleBuilder,
) {
    inline fun <reified T : Any> factory(
        qualifier: Qualifier = TypeQualifier(T::class),
        noinline create: () -> T,
    ) = dependencyModuleBuilder.factory(qualifier, create)

    inline fun <reified T : Any> single(
        qualifier: Qualifier = TypeQualifier(T::class),
        noinline create: () -> T,
    ) = dependencyModuleBuilder.single(qualifier, create)

    inline fun <reified T : Any> scoped(
        qualifier: Qualifier = TypeQualifier(T::class),
        noinline create: () -> T,
    ) {
        dependencyModuleBuilder.factories.add(
            DependencyFactory(
                qualifier,
                CreateRule.SINGLE,
                create,
                scope,
            ),
        )
    }

    inline fun <reified T : Any> get(qualifier: Qualifier = TypeQualifier(T::class)): T =
        dependencyModuleBuilder.appContainerStore.instantiate(qualifier, scope) as T
}
