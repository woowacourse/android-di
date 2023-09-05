package com.ki960213.sheath.scanner

interface ClassScanner {

    fun findAll(targetClass: Class<*>): List<Class<*>>
}
