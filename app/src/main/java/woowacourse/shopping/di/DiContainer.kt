package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaGetter

open class DiContainer {
    private val fields get() = this.javaClass.declaredFields

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

    fun <T : Any> get(clazz: KClass<T>): T {
        return getFromField(clazz)
            ?: getFromGetter(clazz)
            ?: throw IllegalArgumentException()
    }

    private fun <T : Any> getFromField(clazz: KClass<T>): T? {
        return fields.firstOrNull { field ->
            field.isAccessible = true
            field.type.simpleName == clazz.simpleName
        }?.get(this) as T?
    }

    private fun <T : Any> getFromGetter(clazz: KClass<T>): T? {
        return properties.firstOrNull { property ->
            property.isAccessible = true
            property.javaGetter?.returnType?.simpleName == clazz.simpleName
        }?.getter?.call(this) as T?
    }
}
