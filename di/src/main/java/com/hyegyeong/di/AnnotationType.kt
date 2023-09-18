package com.hyegyeong.di

import kotlin.reflect.KClass

data class AnnotationType(val annotation: Annotation? = null, val clazz: KClass<*>)
