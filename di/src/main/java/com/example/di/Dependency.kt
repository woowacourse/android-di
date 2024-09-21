package com.example.di

import com.example.di.annotation.QualifierType
import kotlin.reflect.KClass

data class Dependency(val type: KClass<*>, val qualifierType: QualifierType?)
