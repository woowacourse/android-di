package com.boogiwoogi.di

import kotlin.reflect.KClass

class DefaultWoogiContainer(
    values: List<Dependency<out Any>> = listOf()
) : WoogiContainer {

    private val values: MutableList<Dependency<out Any>> = values.toMutableList()

    override fun <T : Any> declareDependency(dependency: Dependency<T>) {
        values.add(dependency)
    }

    override fun find(clazz: KClass<*>): Any? =
        values.find { it.isTypeOf(clazz) }?.instance

    override fun find(clazzName: String): Any? =
        values.find { it.instance::class.simpleName == clazzName }?.instance
}

fun woogiInitializer(declareDependencies: WoogiContainer.() -> Unit): WoogiInjector =
    WoogiInjector(
        DefaultWoogiContainer().apply { declareDependencies() }
    )
