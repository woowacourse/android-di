package com.daedan.compactAndroidDi.fixture

import com.daedan.compactAndroidDi.AppContainerStore
import com.daedan.compactAndroidDi.DependencyModule
import com.daedan.compactAndroidDi.DependencyModuleBuilder

fun module(
    container: AppContainerStore,
    block: DependencyModuleBuilder.() -> Unit,
): DependencyModule {
    val builder = DependencyModuleBuilder(container)
    block(builder)
    return builder.build()
}
