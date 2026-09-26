package woowacourse.di

import kotlin.reflect.KClass

fun diContainer(block: DIContainer.() -> Unit): DIContainer = DIContainer().apply(block)

inline fun <reified T : Any, reified I : T> DIContainer.bind(qualifier: KClass<out Annotation>? = null) {
    bind(T::class, I::class, qualifier)
}

inline fun <reified T : Any> DIContainer.register(
    instance: T,
    qualifier: KClass<out Annotation>? = null,
) {
    register(T::class, instance, qualifier)
}

inline fun <reified T : Any> DIContainer.get(qualifier: KClass<out Annotation>? = null): T = get(T::class, qualifier)

inline fun <reified T : Any> DIContainer.create(qualifier: KClass<out Annotation>? = null): T = create(T::class, qualifier)
