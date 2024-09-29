package com.example.sh1mj1.extension

import com.example.sh1mj1.annotation.Qualifier
import kotlin.reflect.KAnnotatedElement

fun KAnnotatedElement.withQualifier(): Qualifier? = annotations.filterIsInstance<Qualifier>().firstOrNull()
