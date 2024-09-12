package woowa.shopping.di.libs.inject

import woowa.shopping.di.libs.container.Containers

inline fun <reified T : Any> inject(): Lazy<T> {
    return lazy { Containers.get(T::class) }
}
