package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.javaMethod

open class DiContainer(private val parentDiContainer: DiContainer? = null) {
    private val methods get() = this::class.declaredFunctions

    private val properties get() = this::class.declaredMemberProperties

    fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor = clazz.primaryConstructor ?: throw IllegalArgumentException()

        constructor.isAccessible = true

        return constructor.call(
            *constructor.parameters.map { param ->
                get(param.type.classifier as KClass<*>)
            }.toTypedArray()
        )
    }

    fun <T : Any> get(clazz: KClass<T>): T? {
        return getFromMethod(clazz) ?: getFromGetter(clazz) ?: parentDiContainer?.get(clazz)
    }

    private fun <T : Any> getFromMethod(clazz: KClass<T>): T? {
        return methods.firstOrNull { method ->
            method.isAccessible = true
            method.javaMethod?.returnType?.simpleName == clazz.simpleName
        }?.call(this) as T?
    }

    private fun <T : Any> getFromGetter(clazz: KClass<T>): T? {
        return properties.firstOrNull { property ->
            property.isAccessible = true
            property.javaGetter?.returnType?.simpleName == clazz.simpleName
        }?.getter?.call(this) as T?
    }
}
