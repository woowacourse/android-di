package woowacourse.di

import kotlin.reflect.KClass

fun injector(definitions: Injector.() -> Unit): Injector = Injector().apply(definitions)

inline fun <reified T : Any> Injector.singleton(
    qualifier: KClass<out Annotation>? = null,
    noinline provider: Injector.() -> T,
) {
    registerSingleton(T::class, qualifier, provider)
}

inline fun <reified T : Any> Injector.get(qualifier: KClass<out Annotation>? = null): T = create(T::class, qualifier)
