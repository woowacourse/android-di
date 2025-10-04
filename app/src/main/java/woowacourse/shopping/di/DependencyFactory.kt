package woowacourse.shopping.di

import kotlin.reflect.KClass

class DependencyFactory<T : Any>(
    val type: KClass<T>,
    val create: () -> T,
) {
    operator fun invoke(): T = create.invoke()
}
