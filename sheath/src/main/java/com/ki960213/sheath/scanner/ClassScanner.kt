package com.ki960213.sheath.scanner

interface ClassScanner {

    fun findAll(target: Class<*>): List<Class<*>>
}
