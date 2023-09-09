package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

open class DiContainer(private val parentDiContainer: DiContainer? = null) {

    fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor = getInjectConstructor(clazz)
        val args = constructor.parameters
            .associateWith { parameter -> get(parameter.type.jvmErasure) }
            .filterValues { it != null }
        return constructor.callBy(args)
    }

    private fun <T : Any> getInjectConstructor(clazz: KClass<T>): KFunction<T> {
        if (clazz.constructors.size == 1 && clazz.constructors.first().parameters.isEmpty()) {
            return clazz.constructors.first()
        }
        println(clazz.constructors.count { it.hasAnnotation<DiInject>() })
        if (clazz.constructors.count { it.hasAnnotation<DiInject>() } != 1) {
            throw IllegalArgumentException("DiInject annotation must be on only one constructor")
        }
        return clazz.constructors.first { it.hasAnnotation<DiInject>() }
    }

    fun <T : Any> get(clazz: KClass<T>): T? {
        return getFromMethod(clazz)
            ?: getFromGetter(clazz)
            ?: parentDiContainer?.get(clazz)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getFromMethod(clazz: KClass<T>): T? {
        return this::class.declaredFunctions.firstOrNull { function ->
            function.isAccessible = true
            function.javaMethod?.returnType?.simpleName == clazz.simpleName
        }?.call(this) as T?
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getFromGetter(clazz: KClass<T>): T? {
        return this::class.declaredMemberProperties.firstOrNull { property ->
            property.isAccessible = true
            property.javaGetter?.returnType?.simpleName == clazz.simpleName
        }?.getter?.call(this) as T?
    }
}
