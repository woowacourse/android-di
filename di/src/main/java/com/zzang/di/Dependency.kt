package com.zzang.di

import com.zzang.di.annotation.QualifierType
import kotlin.reflect.KClass

data class Dependency(val type: KClass<*>, val qualifierType: QualifierType?)
