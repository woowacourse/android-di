package com.daedan.compactAndroidDi

import com.daedan.compactAndroidDi.qualifier.Qualifier

class DependencyFactory<T : Any>(
    val qualifier: Qualifier,
    val create: () -> T,
) {
    operator fun invoke(): T = create.invoke()
}
