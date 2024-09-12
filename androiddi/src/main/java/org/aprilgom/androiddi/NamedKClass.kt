package org.aprilgom.androiddi

import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

data class NamedKClass(val name: String, val clazz: KClass<*>) {
    constructor(clazz: KClass<*>) : this(clazz.jvmName, clazz)

    override fun equals(other: Any?): Boolean {
        if (other is NamedKClass) {
            return name == other.name
        }
        return false
    }
    override fun hashCode(): Int {
        return name.hashCode()
    }
}
