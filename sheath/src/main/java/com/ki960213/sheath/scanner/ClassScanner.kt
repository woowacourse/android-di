package com.ki960213.sheath.scanner

import kotlin.reflect.KClass

interface ClassScanner {

    fun findAll(targetClass: KClass<*>): List<KClass<*>>
}
