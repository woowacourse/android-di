package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object DependencyInjector {
    private val dependencyGetters: MutableMap<Identifier, () -> Any> = mutableMapOf()

    fun initialize(vararg module: Module) {
        module.forEach(::initialize)
    }

    fun <T : Any> create(targetClass: KClass<T>): T {
        val instance: T = injectParameters(targetClass)
        injectFields(instance)
        return instance
    }

    private fun initialize(module: Module) {
        module::class.memberProperties.forEach { property: KProperty1<out Module, *> ->
            if (property.findAnnotation<Dependency>() == null) return@forEach

            val identifier = Identifier.of(property)
            dependencyGetters[identifier] = {
                property.getter.call(module) ?: error("${property}의 getter가 null을 반환했습니다.")
            }
        }
    }

    private fun dependency(identifier: Identifier): Any =
        dependencyGetters[identifier]?.invoke() ?: error("${identifier}에 대한 의존성이 정의되지 않았습니다.")

    private fun <T : Any> injectParameters(targetClass: KClass<T>): T {
        val constructor: KFunction<T> =
            targetClass.primaryConstructor ?: error("${targetClass}의 주 생성자를 찾을 수 없습니다.")
        val parameters: Array<Any> = constructor.parameters.map(Identifier::of).toTypedArray()
        return constructor.call(*parameters)
    }

    private fun injectFields(target: Any) {
        target::class.memberProperties.forEach { property: KProperty1<out Any, *> ->
            if (property.findAnnotation<Inject>() == null) return@forEach

            val identifier = Identifier.of(property)
            val mutableProperty =
                property as? KMutableProperty1
                    ?: error("${property}은(는) var이 아니기 때문에 의존성을 주입할 수 없습니다.")
            mutableProperty.setter.call(target, dependency(identifier))
        }
    }
}
