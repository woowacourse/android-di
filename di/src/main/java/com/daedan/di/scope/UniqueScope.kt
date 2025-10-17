package com.daedan.di.scope

data class UniqueScope(
    val keyScope: Scope,
    val id: String,
) : Scope
