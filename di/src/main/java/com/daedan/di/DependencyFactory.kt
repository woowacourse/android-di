package com.daedan.di

import com.daedan.di.qualifier.Qualifier
import com.daedan.di.scope.CreateRule
import com.daedan.di.scope.Scope
import com.daedan.di.scope.SingleTonScope

class DependencyFactory<T : Any>(
    val qualifier: Qualifier,
    val createRule: CreateRule,
    val create: () -> T,
    val scopeQualifier: Scope = SingleTonScope,
) {
    operator fun invoke(): T = create.invoke()
}
