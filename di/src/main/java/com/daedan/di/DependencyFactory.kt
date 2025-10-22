package com.daedan.di

import com.daedan.di.qualifier.Qualifier
import com.daedan.di.scope.CreateRule
import com.daedan.di.scope.Scope
import com.daedan.di.scope.SingleTonScope

class DependencyFactory<T : Any>(
    val qualifier: Qualifier,
    val createRule: CreateRule,
    val create: (Scope) -> T,
    val scope: Scope = SingleTonScope,
) {
    operator fun invoke(scope: Scope): T = create.invoke(scope)
}
