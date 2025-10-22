package com.daedan.di

import com.daedan.di.dsl.DependencyModuleBuilder

data class DependencyModule(
    val factories: List<DependencyFactory<*>>,
)

fun DiComponent.module(block: DependencyModuleBuilder.() -> Unit): DependencyModule = module(appContainerStore, block)

fun module(
    container: AppContainerStore,
    block: DependencyModuleBuilder.() -> Unit,
): DependencyModule {
    val builder = DependencyModuleBuilder(container)
    block(builder)
    return builder.build()
}
