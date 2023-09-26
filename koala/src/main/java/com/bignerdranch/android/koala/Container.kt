package com.bignerdranch.android.koala

import kotlin.reflect.KClass

object Container {

    val instances = mutableMapOf<KClass<*>, Any?>()

    val annotations = mutableMapOf<Annotation, KClass<*>>()
}
