package com.ki960213.sheath.util

import java.lang.reflect.Constructor

object InstanceGenerator {

    fun generate(clazz: Class<*>, instances: List<Any>): Any {
        val primaryConstructor = clazz.primaryConstructor()

        if (primaryConstructor == null || primaryConstructor.parameterCount <= 0) return clazz.newInstance()
        val initArgs = primaryConstructor.parameters.map {
            val findInstances = instances.filter { instance -> it.type.isInstance(instance) }
            when {
                findInstances.isEmpty() -> throw IllegalStateException("${clazz.name}의 종속 항목인 ${it.type.name}의 인스턴스가 존재하지 않습니다.")
                findInstances.size >= 2 -> throw IllegalStateException("${clazz.name}의 종속 항목인 ${it.type.name}의 인스턴스가 여러 개 존재합니다.")
            }
            findInstances[0]
        }
        return primaryConstructor.newInstance(*initArgs.toTypedArray())
    }

    private fun Class<*>.primaryConstructor(): Constructor<*>? =
        this.declaredConstructors.firstOrNull()
}
