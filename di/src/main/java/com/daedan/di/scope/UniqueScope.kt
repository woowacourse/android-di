package com.daedan.di.scope

import java.util.UUID

data class UniqueScope(
    val keyScope: Scope,
    val id: String = UUID.randomUUID().toString(),
) : Scope
