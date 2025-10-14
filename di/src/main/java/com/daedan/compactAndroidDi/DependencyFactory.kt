package com.daedan.compactAndroidDi

class DependencyFactory<T : Any>(
    val qualifier: Qualifier,
    val create: () -> T,
) {
    operator fun invoke(): T = create.invoke()
}
