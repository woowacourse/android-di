package com.daedan.di

import com.daedan.di.qualifier.Qualifier
import com.daedan.di.scope.CreateRule

class DependencyFactory<T : Any>(
    val qualifier: Qualifier,
    val createRule: CreateRule,
    val create: () -> T,
) {
    operator fun invoke(): T = create.invoke()
}
