package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.javaMethod

open class DiContainer(private val parentDiContainer: DiContainer? = null) {

    fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor = clazz.primaryConstructor ?: throw IllegalArgumentException()
        val args = constructor.parameters.map { param -> get(param.type.classifier as KClass<*>) }

        return constructor.call(*args.toTypedArray())
    }

    fun <T : Any> get(clazz: KClass<T>): T? {
        return getFromMethod(clazz) ?: getFromGetter(clazz) ?: parentDiContainer?.get(clazz)
    }

    private fun <T : Any> getFromMethod(clazz: KClass<T>): T? {
        return this::class.declaredFunctions.firstOrNull { method ->
            method.javaMethod?.returnType?.simpleName == clazz.simpleName
        }?.call(this) as T?
    }

    private fun <T : Any> getFromGetter(clazz: KClass<T>): T? {
        return this::class.declaredMemberProperties.firstOrNull { property ->
            property.javaGetter?.returnType?.simpleName == clazz.simpleName
        }?.getter?.call(this) as T?
    }
}
