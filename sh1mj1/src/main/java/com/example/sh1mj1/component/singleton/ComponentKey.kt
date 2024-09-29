package com.example.sh1mj1.component.singleton

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1

@Suppress("DataClassPrivateConstructor")
data class ComponentKey private constructor(
    val clazz: KClass<*>,
    val qualifier: Qualifier? = null,
) {
    companion object {
        private val cache = mutableMapOf<Pair<KClass<*>, Qualifier?>, ComponentKey>()
        fun of(
            clazz: KClass<*>,
            qualifier: Qualifier? = null,
        ): ComponentKey {
            val key = clazz to qualifier
            return cache.getOrPut(key) {
                ComponentKey(clazz, qualifier)
            }
        }

        fun fromParameter(parameter: KParameter): ComponentKey = of(
            clazz = parameter.type.classifier as KClass<*>,
            qualifier = parameter.withQualifier()
        )

        fun fromProperty(property: KProperty1<*, *>): ComponentKey = of(
            clazz = property.returnType.classifier as KClass<*>,
            qualifier = property.withQualifier()
        )
    }
}

