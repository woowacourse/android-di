package com.example.sh1mj1.extension

import com.example.sh1mj1.Qualifier
import kotlin.reflect.KParameter

fun KParameter.withQualifier(): Qualifier? = annotations.filterIsInstance<Qualifier>().firstOrNull()
