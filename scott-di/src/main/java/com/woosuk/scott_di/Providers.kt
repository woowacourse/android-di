package com.woosuk.scott_di

import kotlin.reflect.KClass

class Providers(
    private val _values: MutableList<Provider> = mutableListOf()
) {
    val values: List<Provider>
        get() = _values.toList()

    fun addService(provider: Provider) {
        this._values.add(provider)
    }

    fun getInstance(kClazz: KClass<*>, annotation: Annotation?): Any {
        values.forEach { println("Scott ${it.typeClass} ${kClazz} ${it.qualifierAnnotation} ${annotation}") }
        return values.find { it.typeClass == kClazz && it.qualifierAnnotation == annotation }
            ?.getInstance(this)
            ?: throw IllegalStateException("${annotation!!.annotationClass.simpleName}일치하는 어노테이션이 없습니다")
    }
}

