package com.zzang.di

import com.zzang.di.annotation.QualifierType
import com.zzang.di.module.DIModule
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    private val singletonInstances = mutableMapOf<String, Any>()
    private val interfaceMappings = mutableMapOf<String, KClass<*>>()

    fun loadModule(module: DIModule) {
        module.register(this)
    }

    private fun buildKey(type: KClass<*>, qualifier: QualifierType): String {
        return "${type.qualifiedName}_${qualifier.name}"
    }

    fun <T : Any> registerInstance(
        interfaceClass: KClass<T>,
        instance: T,
        qualifier: QualifierType = QualifierType.DATABASE
    ) {
        val key = buildKey(interfaceClass, qualifier)
        singletonInstances[key] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(type: KClass<T>, qualifier: QualifierType = QualifierType.DATABASE): T {
        val key = buildKey(type, qualifier)
        singletonInstances[key]?.let { return it as T }

        val implementationType = interfaceMappings[key] ?: type

        val constructor = implementationType.primaryConstructor
            ?: throw IllegalArgumentException("${implementationType.simpleName} 클래스의 인스턴스를 생성할 수 없습니다. 생성자가 없습니다.")

        val parameters = constructor.parameters.map { parameter ->
            val parameterType = parameter.type.classifier as KClass<*>
            resolve(parameterType)
        }

        val instance = constructor.call(*parameters.toTypedArray()) as T
        registerInstance(type, instance, qualifier)
        return instance
    }
}
