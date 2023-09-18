package com.dygames.di.model

import kotlin.reflect.KType

data class LifecycleAwareProviders(
    val value: HashMap<KType?, QualifiableProviders> = hashMapOf(null to QualifiableProviders())
)
