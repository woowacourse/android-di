package com.dygames.di.model

import kotlin.reflect.KType

data class LifecycleAwareDependencies(
    val value: HashMap<KType?, QualifiableDependencies> = hashMapOf()
)
