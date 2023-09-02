package woowacourse.shopping.di

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaGetter

open class DiContainer {
    private val fields get() = this.javaClass.declaredFields

    private val properties get() = this::class.declaredMemberProperties

    private fun <T> getFromField(clazz: Class<T>): T? {
        return fields.firstOrNull { field ->
            field.isAccessible = true
            field.type.simpleName == clazz.simpleName
        }?.get(this) as T
    }

    private fun <T> getFromGetter(clazz: Class<T>): T? {
        return properties.firstOrNull { property ->
            property.isAccessible = true
            property.javaGetter?.returnType?.simpleName == clazz.simpleName
        }?.getter?.call(this) as T
    }

    fun <T> get(clazz: Class<T>): T {
        return getFromField(clazz)
            ?: getFromGetter(clazz)
            ?: throw IllegalArgumentException()
    }

    fun <T> inject(clazz: Class<T>): T {
        val constructor = clazz.declaredConstructors.first()

        return constructor.newInstance(
            *constructor.parameterTypes.map { get(it) }.toTypedArray()
        ) as T
    }
}
