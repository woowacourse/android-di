package com.ki960213.sheath.instantiater

import java.lang.reflect.Constructor

fun Class<*>.instantiateByPrimaryConstructor(instances: List<Any>): Any {
    val primaryConstructor = this.primaryConstructor()
    if (primaryConstructor == null || primaryConstructor.parameterCount <= 0) return this.newInstance()

    val initArgs = primaryConstructor.parameters.map { instances.getInstanceOf(it.type) }
    return primaryConstructor.newInstance(*initArgs.toTypedArray())
}

private fun Class<*>.primaryConstructor(): Constructor<*>? = this.declaredConstructors.firstOrNull()

private fun List<Any>.getInstanceOf(clazz: Class<*>): Any {
    return this.find { instance -> clazz.isInstance(instance) }
        ?: throw IllegalArgumentException("인스턴스 목록에 ${clazz.name} 인스턴스가 존재하지 않습니다.")
}
