package com.ki960213.sheath.instantiater

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor

fun KClass<*>.instantiateByPrimaryConstructor(instances: List<Any>): Any {
    val primaryConstructor = this.primaryConstructor ?: return this.createInstance()

    val initArgs = primaryConstructor.parameters.map { instances.getInstanceOf(it.type) }
    return primaryConstructor.call(*initArgs.toTypedArray())
}

private fun List<Any>.getInstanceOf(type: KType): Any {
    return this.find { instance -> type.isSupertypeOf(instance.javaClass.kotlin.createType()) }
        ?: throw IllegalArgumentException("인스턴스 목록에 $type 인스턴스가 존재하지 않습니다.")
}
