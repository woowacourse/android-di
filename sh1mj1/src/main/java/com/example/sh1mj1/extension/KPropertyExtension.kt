package com.example.sh1mj1.extension

import com.example.sh1mj1.Qualifier
import kotlin.reflect.KProperty1

fun <T> KProperty1<T, *>.withQualifier(): Qualifier? = annotations.filterIsInstance<Qualifier>().firstOrNull()
