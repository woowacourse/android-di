package com.now.di

import kotlin.reflect.KClass

data class DependencyType(val klass: KClass<*>, val annotation: Annotation?)
