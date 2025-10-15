package com.daedan.compactAndroidDi

import com.daedan.compactAndroidDi.qualifier.Qualifier
import com.daedan.compactAndroidDi.scope.CreateRule

class DependencyFactory<T : Any>(
    val qualifier: Qualifier,
    val createRule: CreateRule,
    val create: () -> T,
) {
    operator fun invoke(): T = create.invoke()
}
